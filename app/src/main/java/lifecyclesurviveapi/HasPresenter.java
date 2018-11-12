package lifecyclesurviveapi;

public interface HasPresenter<P extends Presenter> {
    P getPresenter();
}
