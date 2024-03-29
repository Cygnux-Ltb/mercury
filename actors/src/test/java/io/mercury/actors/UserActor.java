package io.mercury.actors;

import akka.actor.Props;
import io.mercury.actors.def.BaseActorT1;

public class UserActor extends BaseActorT1<User> {
//    private Object parameters;

    public static Props props() {
        return Props.create(UserActor.class, UserActor::new);
    }

    // 如果有构造参数, 便按照如下方式构造 Props 即可
//    public static Props props(Object parameters) {
//        return Props.create(UserLoginActor.class, () -> new UserLoginActor(parameters));
//    }
//
//    private UserLoginActor(Object parameters) {
//        this.parameters = parameters;
//    }

    @Override
    protected void onEvent(User user) {
        System.out.println("received user -> userId==[" + user.userId() + "], userName==[" + user.userName()
                + "], age==[" + user.age() + "]");
    }

    @Override
    protected Class<User> eventType() {
        return User.class;
    }

    @Override
    protected void handleUnknown0(Object t) {

    }

}