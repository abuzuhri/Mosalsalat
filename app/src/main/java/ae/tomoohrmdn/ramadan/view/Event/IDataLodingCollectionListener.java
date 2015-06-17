package ae.tomoohrmdn.ramadan.view.Event;

/**
 * Created by tareq on 06/12/2015.
 */
public interface IDataLodingCollectionListener<T> {
    public void onSeccess(boolean isChanged);
    public void onFail(String error);
}