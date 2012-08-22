package org.resthub.web.support;

import com.ning.http.client.AsyncCompletionHandler;
import java.util.ArrayList;
import java.util.List;
import org.resthub.web.Response;

public class AsyncEntityHandler extends AsyncCompletionHandler<Response> {

    protected List<BodyReader> bodyReaders = new ArrayList<>();

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
