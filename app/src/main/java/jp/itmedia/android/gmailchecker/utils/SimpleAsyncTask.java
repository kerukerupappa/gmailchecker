package jp.itmedia.android.gmailchecker.utils;

import android.os.AsyncTask;

/**
 * Created by tkamada on 2014/04/24.
 */
public abstract class SimpleAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    @Override
    protected Result doInBackground(Params... params) {
        return doInBackground(params.length > 0 ? params[0] : null);
    }

    abstract protected Result doInBackground(Params param);

    @Override
    protected void onProgressUpdate(Progress... values) {
        onProgressUpdate(values.length > 0 ? values[0] : null);
    }

    protected void onProgressUpdate(Progress value) {
    }

    @SuppressWarnings({"unchecked"})
    protected final void publishProgress(Progress value) {
        super.publishProgress(value);
    }
}
