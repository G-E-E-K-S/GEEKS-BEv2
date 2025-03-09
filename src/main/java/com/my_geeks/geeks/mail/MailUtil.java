package com.my_geeks.geeks.mail;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.my_geeks.geeks.aws.AwsSesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailUtil {
    private final AwsSesConfig awsSesConfig;

    public void send(String email, String code) {
        String html = "<!DOCTYPE html>\n" +
                "    <table align=\"center\" style=\"width: 100%; padding: 0 30px 40px 30px;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "        <tbody>\n" +
                "            <tr>\n" +
                "                <td align=\"center\" valign=\"top\">\n" +
                "                    <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%; max-width: 620px;min-width: 300px;\" border=\"0\">\n" +
                "                        <tbody>\n" +
                "                            <tr>\n" +
                "                                <td align=\"center\" style=\"background-color: #FFD540; height: 180px; padding: 0;\">\n" +
                "                                    <img style=\"margin: 69px 0;\" src=\"https://bucket-geeks.s3.ap-northeast-2.amazonaws.com/logo.svg\" alt=\"긱스 로고\"/>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td align=\"center\" style=\"padding: 0;\">\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width: 95%; margin: 0 auto; background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td style=\"padding: 0 1rem;\">\n" +
                "                                                <div style=\"color: #525252; font-size: 1.2rem; font-weight: 500; margin-top: 9.19vh;\">\n" +
                "                                                    안녕하세요, 긱스에서 요청하신 인증번호를 보내드려요.\n" +
                "                                                </div>\n" +
                "                                                <div style=\"color: #333; font-size: 3.3rem; font-weight: 500; line-height: normal; margin-top: 4rem; margin-bottom: 2rem;\">\n" +
                "                                                    " + code + "\n" +
                "                                                </div>\n" +
                "                                                <div style=\"color: #525252; font-size: 1.25rem; font-style: normal; font-weight: 500;\">\n" +
                "                                                    위 인증번호 4자리를 인증번호 입력창에 정확히 입력해주세요.\n" +
                "                                                </div>\n" +
                "                                                <div style=\"color: #525252; font-size: 1rem; font-style: normal; font-weight: 500; margin-top: 1rem; margin-bottom: 3rem;\">\n" +
                "                                                    인증번호를 요청하지 않았을 경우 본 이메일을 무시해 주세요.\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </tbody>\n" +
                "                    </table>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "</html>";
        try {
            AmazonSimpleEmailService emailService = awsSesConfig.amazonSimpleEmailService();

            String from = "geeks <no-reply@my-geeks.com>";
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
