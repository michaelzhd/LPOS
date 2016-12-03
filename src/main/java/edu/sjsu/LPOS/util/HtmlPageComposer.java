package edu.sjsu.LPOS.util;


import edu.sjsu.LPOS.domain.User;



public class HtmlPageComposer {
	
    public static String registerConfirmation(User user, String code, String mailServiceServer, int mailServicePort, int expireInHours) {
        String page = "<html><body>"
                + "<h3>Dear " + user.getUsername() + ",</h3>"
                + "<br /> Congratulation! Your account is waiting for your confirmation."
                + "<h3>Please click the following link to activate:</h3>"
                + "<a href='http://"
                + mailServiceServer
                + ":"
                + mailServicePort
                + "/user/register/confirm?id=" + user.getId() + "&code="+ code +"'>Click me</a> <br />"
                + "The link above will expire in "
                + expireInHours
                + " hours"
                + "<h3>Sincerely</h3>"
                + "<h3>Lunch Planning and Ordering Service Team</h3>"
                + "</body></html>";
        return page;
    }
}
