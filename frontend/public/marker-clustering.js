/**
 * Naver Maps V3 Marker Clustering
 * Copyright 2016 NAVER Corp.
 * MIT License
 *
 * @version 2.1.2
 * @author naver
 *
 * Patched for modern API compatibility
 */
var MarkerClustering = function(options) {
    this._map = options.map;
    this._markers = [];
    this._clusters = [];
    this._maxZoom = options.maxZoom || 16;
    this._minClusterSize = options.minClusterSize || 2;
    this._icons = options.icons || [];
    this._indexGenerator = options.indexGenerator || [10, 100, 200, 500, 1000];
    this._gridSize = options.gridSize || 100;
    this._averageCenter = options.averageCenter || false;
    this._listeners = [];

    this.addMarkers(options.markers || []);

    // Patched: Use naver.maps.Event.addListener
    this._listeners.push(naver.maps.Event.addListener(this._map, 'idle', this._redraw.bind(this)));
};

MarkerClustering.prototype = {
    constructor: MarkerClustering,

    addMarkers: function(markers) {
        for (var i = 0, ii = markers.length; i < ii; i++) {
            this.addMarker(markers[i], true);
        }
        this._redraw();
    },

    addMarker: function(marker, nodraw) {
        if (this._getMarkerIndex(marker) > -1) return;
        marker.setMap(null);
        this._markers.push(marker);
        if (!nodraw) {
            this._redraw();
        }
    },

    removeMarker: function(marker, nodraw) {
        var index = this._getMarkerIndex(marker);
        if (index === -1) return;
        marker.setMap(null);
        this._markers.splice(index, 1);
        if (!nodraw) {
            this._redraw();
        }
    },

    removeMarkers: function(markers, nodraw) {
        for (var i = 0, ii = markers.length; i < ii; i++) {
            this.removeMarker(markers[i], true);
        }
        if (!nodraw) {
            this._redraw();
        }
    },

    _getMarkerIndex: function(marker) {
        return this._markers.indexOf(marker);
    },

    setMap: function(map) {
        if (this._map) {
            this._clear();
            naver.maps.Event.removeListener(this._listeners);
            this._listeners = [];
        }
        this._map = map;
        if (map) {
            this._listeners.push(naver.maps.Event.addListener(this._map, 'idle', this._redraw.bind(this)));
            this._redraw();
        }
    },

    _redraw: function() {
        if (!this._map) return;
        this._clear();

        var mapBounds = this._map.getBounds();

        for (var i = 0, ii = this._markers.length; i < ii; i++) {
            var marker = this._markers[i];
            if (mapBounds.hasLatLng(marker.getPosition())) {
                this._addToClosestCluster(marker);
            }
        }
        this._updateClusters();
    },

    _clear: function() {
        for (var i = 0, ii = this._clusters.length; i < ii; i++) {
            this._clusters[i].destroy();
        }
        this._clusters = [];
    },

    _addToClosestCluster: function(marker) {
        var distance = Infinity,
            closestCluster = null;

        for (var i = 0, ii = this._clusters.length; i < ii; i++) {
            var cluster = this._clusters[i];
            var center = cluster.getCenter();

            if (center) {
                // 👇 여기 수정됨
                var proj = this._map.getProjection();
                var centerPoint = proj.fromCoordToOffset(center);
                var markerPoint = proj.fromCoordToOffset(marker.getPosition());
                var d = Math.sqrt(
                    Math.pow(centerPoint.x - markerPoint.x, 2) +
                    Math.pow(centerPoint.y - markerPoint.y, 2)
                );

                if (d < distance) {
                    distance = d;
                    closestCluster = cluster;
                }
            }
        }

        if (closestCluster && closestCluster.isCenterInGrid(marker, this._gridSize)) {
            closestCluster.addMarker(marker);
        } else {
            var newCluster = new Cluster(this);
            newCluster.addMarker(marker);
            this._clusters.push(newCluster);
        }
    },


    _updateClusters: function() {
        for (var i = 0, ii = this._clusters.length; i < ii; i++) {
            this._clusters[i].update();
        }
    },

    getIcon: function(count) {
        var index = this._getIndex(count);
        return this._icons[index];
    },

    _getIndex: function(count) {
        var index = 0;
        for (var i = 0, ii = this._indexGenerator.length; i < ii; i++) {
            if (count >= this._indexGenerator[i]) {
                index = i + 1;
            }
        }
        return index;
    }
};

var Cluster = function(markerClusterer) {
    this._markerClusterer = markerClusterer;
    this._map = markerClusterer._map;
    this._markers = [];
    this._clusterMarker = null;
    this._center = null;
};

Cluster.prototype = {
    constructor: Cluster,
    addMarker: function(marker) {
        this._markers.push(marker);
        this._center = null;
    },
    getCenter: function() {
        if (!this._center && this._markers.length > 0) {
            var lat = 0, lng = 0;
            for (var i = 0, ii = this._markers.length; i < ii; i++) {
                var pos = this._markers[i].getPosition();
                lat += pos.lat();
                lng += pos.lng();
            }
            this._center = new naver.maps.LatLng(lat / this._markers.length, lng / this._markers.length);
        }
        return this._center;
    },
    update: function() {
        var count = this._markers.length;
        if (count < this._markerClusterer._minClusterSize) {
            for (var i = 0; i < count; i++) {
                this._markers[i].setMap(this._map);
            }
            if (this._clusterMarker) {
                this._clusterMarker.setMap(null);
            }
            return;
        }

        if (!this._clusterMarker) {
            this._clusterMarker = new naver.maps.Marker({
                map: this._map,
                cursor: 'pointer'
            });
        }

        var icon = this._markerClusterer.getIcon(count);
        if (typeof icon.content === 'string') {
            icon.content = icon.content.replace('{text}', count);
        }

        this._clusterMarker.setIcon(icon);
        this._clusterMarker.setPosition(this.getCenter());

        for (var i = 0; i < count; i++) {
            this._markers[i].setMap(null);
        }

        naver.maps.Event.clearInstanceListeners(this._clusterMarker);
        naver.maps.Event.addListener(this._clusterMarker, 'click', this._onClick.bind(this));
    },
    _onClick: function() {
        if (this._map.getZoom() < this._markerClusterer._maxZoom) {
            var bounds = new naver.maps.LatLngBounds();
            for (var i = 0, ii = this._markers.length; i < ii; i++) {
                bounds.extend(this._markers[i].getPosition());
            }
            this._map.fitBounds(bounds);
        }
    },
    destroy: function() {
        if (this._clusterMarker) {
            this._clusterMarker.setMap(null);
        }
        for (var i = 0, ii = this._markers.length; i < ii; i++) {
            this._markers[i].setMap(this._map);
        }
    },
    isCenterInGrid: function(marker, gridSize) {
        var center = this.getCenter();
        if (!center) return false;
        var proj = this._map.getProjection();
        var centerPx = proj.fromCoordToOffset(center);
        var markerPx = proj.fromCoordToOffset(marker.getPosition());
        return Math.abs(centerPx.x - markerPx.x) <= gridSize && Math.abs(centerPx.y - markerPx.y) <= gridSize;
    }
};