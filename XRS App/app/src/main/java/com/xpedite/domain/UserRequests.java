package com.xpedite.domain;

import java.util.List;

/**
 * Created by abhinkum on 9/18/18.
 */

public class UserRequests {
    public List<UserRequest> getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(List<UserRequest> userRequest) {
        this.userRequest = userRequest;
    }

    private List<UserRequest> userRequest;
}
