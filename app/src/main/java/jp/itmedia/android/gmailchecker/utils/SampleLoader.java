package jp.itmedia.android.gmailchecker.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class SampleLoader extends AsyncTaskLoader<String> {

    /** 引数 */
    private String mArg;

    /** 非同期処理での結果 */
    private String mData;

    public SampleLoader(Context context, String arg) {
        super(context);
        mArg = arg;
    }

    @Override
    public String loadInBackground() {
        // 実際の非同期処理

        try {
            // 適当に時間がかかる処理
            Thread.sleep(3000);
            mData = "hoge";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mData;
    }

    @Override
    protected void onStartLoading() {
        // 非同期処理開始前

        // ActivityまたはFragment復帰時（バックキーで戻った、ホーム画面から戻った等）に
        // 再実行されるためここで非同期処理を行うかチェック

        if (mData != null) {
            // 結果がnullではないは場合は既に実行済みとして非同期処理は不要
            // deliverResultで結果を送信
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            // takeContentChanged いまいちよく分からない
            // CursorLoaderの時に意味があるのかも…

            // 非同期処理を開始
            forceLoad();
        }

    }


    @Override
    protected void onStopLoading() {
        // Loader停止時の処理

        // 非同期処理のキャンセル
        cancelLoad();
    }

    @Override
    public void deliverResult(String data) {
        // 登録してあるリスナー（LoaderCallbacksを実装したクラス）に結果を送信する

        if (isReset()) {
            // Loaderがリセット状態かどうか
            // trueの場合はLoaderがまだ一度も開始していない、resetメソッドが呼ばれている
            return;
        }

        mData = data;

        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        // reset呼び出し時、Loader破棄時の処理
        super.onReset();

        // Loaderを停止
        onStopLoading();

        // データをクリア
        mData = null;
    }

}
