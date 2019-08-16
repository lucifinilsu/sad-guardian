package com.sad.assistant.live.guardian.compiler;

public class AuthenticationFuture {

    private boolean successful=false;
    private Object o=null;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    void set(Object o) {
        this.o = o;
    }

    <A> A get(){
        return (A) o;
    }

}
