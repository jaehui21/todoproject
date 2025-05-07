package com.shinhan.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {
	private int user_id;
	private String name;
	private String email;
	private String password;
	private String nickname;
}