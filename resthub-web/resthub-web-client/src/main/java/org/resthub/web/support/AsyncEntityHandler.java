package org.resthub.web.support;

import com.ning.http.client.AsyncCompletionHandler;
import org.resthub.web.Response;

import java.util.ArrayList;
import java.util.List;

public class AsyncEntityHandler extends AsyncCompletionHandler<Response> {

    protected List<BodyReader> bodyReaders = new ArrayList<BodyReader>();

    public void setBodyReaders(List<BodyReader> brs) {
        this.bodyReaders = brs;
    }

    public void addBodyReader(BodyReader br) {
        this.bodyReaders.add(br);
    }

    @Override
    public Response onCompleted(com.ning.http.client.Response rspns) throws Exception {

        Response resp = new Response(rspns);
        resp.addBodyReaders(this.bodyReaders);

        return resp;
    }

}
