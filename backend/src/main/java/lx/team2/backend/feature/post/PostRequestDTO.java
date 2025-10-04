package lx.team2.backend.feature.post;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PostRequestDTO {
	PostVO post;
	BoardVO board;
}
