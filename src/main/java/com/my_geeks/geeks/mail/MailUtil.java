package com.my_geeks.geeks.mail;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.my_geeks.geeks.aws.AwsSesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {
    private static AwsSesConfig awsSesConfig = null;

    public MailUtil(AwsSesConfig awsSesConfig) {
        MailUtil.awsSesConfig = awsSesConfig;
    }

    public static void send(String email) {
        String html =
                "<div style=\"width: 100vw;max-width: 100%;box-sizing: border-box; height:180px; right:0; left:0; top:0; background-color: #FFD540; margin: 0; padding: 0;display: flex;justify-content: center;align-items: center;\">\n" +
                        "    <img style=\"margin: 69px 0;\" src=\"https://bucket-geeks.s3.ap-northeast-2.amazonaws.com/logo.svg\"/>\n" +
                        "  </div>\n" +
                        "  <div style=\"width: 95vw;margin: 0 auto;padding: 0;background-color: #fff;box-sizing: border-box;\">\n" +
                        "    <div style=\"padding: 0px 1rem\">\n" +
                        "      <div style=\"color: #525252;font-size: 1.2rem;font-weight: 500;margin-top: 9.19vh;\">\n" +
                        "        안녕하세요, 긱스에서 요청하신 인증번호를 보내드려요.\n" +
                        "      </div>\n" +
                        "      <div style=\"color: #333;font-size: 3.3rem;font-weight: 500;line-height: normal;margin-top: 4rem;margin-bottom: 2rem;\">"+1234+"</div>\n" +
                        "      <div style=\"color: #525252;font-size: 1.25rem;font-style: normal;font-weight: 500;\">위 인증번호 4자리를 인증번호 입력창에 정확히 입력해주세요.</div>\n" +
                        "      <div style=\"color: #525252;font-size: 1rem;font-style: normal;font-weight: 500;margin-top: 1rem;margin-bottom:3rem;\">인증번호를 요청하지 않았을 경우 본 이메일을 무시해 주세요.</div>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "  <div style=\"background-color: #FFD540;height: 15vh;padding: 10px 1rem 0px 1rem;width: 100vw;box-sizing: border-box;position: absolute;bottom: 0;max-width: 100%;\">\n" +
                        "    <div style=\"color: #525252;font-size: 1rem;font-style: normal;font-weight: 500;margin-top: 2.46vh;\">인증번호를 요청하지 않았을 경우 본 이메일을 무시해 주세요.</div>\n" +
                        "  </div>\n";
        try {
            AmazonSimpleEmailService emailService = awsSesConfig.amazonSimpleEmailService();

            String from = "Geeks <no-reply@my-geeks.com>";
            Destination destination = new Destination().withToAddresses(email);
            Content subject = new Content().withCharset("UTF-8").withData("회원가입 이메일 인증코드 발송");
            Content body = new Content().withCharset("UTF-8").withData(html);

            Message message = new Message()
                    .withSubject(subject)
                    .withBody(new Body().withHtml(body));

            SendEmailRequest emailRequest = new SendEmailRequest()
                    .withDestination(destination)
                    .withSource(from)
                    .withMessage(message);

            emailService.sendEmail(emailRequest);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
