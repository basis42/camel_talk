package de.guj.dvs.camel;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import twitter4j.Status;

public class DemoRouteBuilder extends RouteBuilder {

    public void configure() {

        String hashtag = "tchh18";
        String TWITTER_STREAM_API_ENDPOINT
                = String.format("twitter-streaming:filter?keywords=%s&type=event&delay=600000&count=100&consumerKey=%s&consumerSecret=%s&accessToken=%s&accessTokenSecret=%s", hashtag, Props.TW_CK, Props.TW_CS, Props.TW_AT, Props.TW_ATS);
        String S3_ENDPOINT
                = String.format("aws-s3://rutz2-camel-talk?accessKey=%s&secretKey=%s", Props.AWS_AK, Props.AWS_SK);

        from(TWITTER_STREAM_API_ENDPOINT)
            .process(exchange -> {
                Message message = exchange.getIn();
                Status status =  message.getBody(Status.class);
                message.setHeader("user", status.getUser().getName());
                message.setHeader("hashtag", hashtag);
                message.setHeader("date", status.getCreatedAt());
                message.setHeader("img", status.getUser().getMiniProfileImageURLHttps());
                message.setBody(status.getText());
            })

            .to("mustache:latestTweet.mustache")

            .setHeader("CamelAwsS3Key", simple("latest_tweet.html"))
            .setHeader("CamelAwsS3ContentType", simple("text/html"))
            .to(S3_ENDPOINT);
    }

}
