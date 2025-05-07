package com.shinhan.friend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FriendDTO {
	private int friend_id;
	private int user_id;
	private int friend_user_id;
	private String nickname;
}
