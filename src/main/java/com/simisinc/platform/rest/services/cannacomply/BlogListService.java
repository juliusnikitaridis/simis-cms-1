package com.simisinc.platform.rest.services.cannacomply;


import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import okhttp3.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */
public class BlogListService {

    private static Log LOG = LogFactory.getLog(BlogListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {

            String xmlContentFromOneView = getContentFfromOneView();
            XStream xstream = new XStream();
           // xstream.addImplicitCollection(channel.class,"contactDetailsList");
            xstream.processAnnotations(rss.class);
            xstream.allowTypesByWildcard(new String[]{"com.simisinc.platform.rest.services.cannacomply.**"});
            xstream.ignoreUnknownElements();
            rss channelList = (rss)xstream.fromXML(xmlContentFromOneView);

            ServiceResponse response = new ServiceResponse(200);
           // ServiceResponseCommand.addMeta(response, "Block List", blockList, null);
           // response.setData(blockList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in BlogListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

    public String getContentFfromOneView() throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        Request request = new Request.Builder()
                .url("https://app.ddc-as.com/rv/alertfeed/rss_2.0?projectId=92F6041E-6A3F-3A93-F9C9-D12EFAF89D48")
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        return responseString;
    }
}


class content {
    public String url;
}

class item {
    public String title;
    public String link;
    public String description;
    //public Date pubDate;
    public String guid;
   // public Date date;
    public String encoded;
    public List<content> content;
}

class channel {
    public String title;
    public String link;
    public String description;
    @XStreamAlias("dc:creator")
    public String creator;
    @XStreamImplicit
    public List<item> item;
}


@XStreamAlias("rss")
class rss {
    public channel channel;
    public String content;
    public String media;
    public String dc;
    public double version;
    public String text;
}

