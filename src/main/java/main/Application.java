package main;

import Bean.*;
import com.alibaba.fastjson.JSON;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.nio.cs.US_ASCII;
import sun.rmi.runtime.Log;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@SpringBootApplication
public class Application {
    private static Connection c = null;
    private static Statement stmt = null;


    @RequestMapping("/now")
    public String now() {
        return "现在时间：" + (new Date()).toString();
    }

    private String getTime() {
        return (new Date()).toString();
    }

    @RequestMapping(value = {"/login"}, method = {RequestMethod.POST})
    @ResponseBody
    public String creatUserByMap(Login login) throws SQLException {
        System.out.println(getTime() + "--->" + "create a new user" + "--->" + login.getName());
        String sql_i = "INSERT INTO USER_BASE_INFO (NAME,PASSWORD,EMAIL,NLANGUAGE,SLANGUAGE,LEVEL,PICURL,USERID,INTEREST,TOPERSON,TOGOAL)" +
                "VALUES ('" + login.getName() + "'," + "'" + login.getPassword() + "'," + "'" + login.getEmail() + "'," + "'" + login.getNativeLanguage()
                + "'," + "'" + login.getStudyLanguage() + "'," + login.getLevel() + "," + "'" + login.getPicture() + "'," + "'" + login.getUserID()
                + "'," + "'" + login.getInterest() + "'," + "'" + login.getToPerson() + "'," + "'" + login.getToGoal() + "');";
        stmt.executeUpdate(sql_i);
        c.commit();
        System.out.println(login.getToGoal());
        return "{\"status\":\"ok\"}";
    }


    @RequestMapping(value = {"/logon"}, method = {RequestMethod.POST})
    @ResponseBody
    public String check(Logon logon) throws SQLException {
        String sql = "SELECT * FROM USER_BASE_INFO WHERE USERID = " + logon.getUserID();
        ResultSet rs = stmt.executeQuery(sql);
        String pw = rs.getString("password");
        System.out.println(getTime() + "--->" + logon.getUserID() + "--->Logon");
        if (pw.equals(logon.getPassword())) {
            Login login = new Login();
            login.setUserID(rs.getString("USERID"));
            login.setName(rs.getString("name"));
            login.setPassword(rs.getString("PASSWORD"));
            login.setEmail(rs.getString("email"));
            login.setNativeLanguage(rs.getString("NLANGUAGE"));
            login.setStudyLanguage(rs.getString("SLANGUAGE"));
            login.setLevel(rs.getString("level"));
            login.setPicture(rs.getString("PICURL"));
            String json = JSON.toJSONString(login);
            return json;
        } else {
            return "{\"status\":\"no\"}";
        }
    }


    @RequestMapping(value = "/cpassword", method = {RequestMethod.POST})
    @ResponseBody
    public String changePassword(ChangePassword changePassword) throws SQLException {
        String sql = "SELECT * FROM USER_BASE_INFO WHERE USERID = " + changePassword.getUserID();
        ResultSet rs = stmt.executeQuery(sql);
        String pw = rs.getString("PASSWORD");
        System.out.println(getTime() + "--->" + changePassword.getUserID() + " change password");
        if (pw.equals(changePassword.getPassword())) {
            String new_sql = "UPDATE USER_BASE_INFO SET PASSWORD = " + changePassword.getNewPassword() + " WHERE USERID = " + changePassword.getUserID() + ";";
            stmt.executeUpdate(new_sql);
            return "{\"status\":\"ok\"}";
        } else {
            return "{\"status\":\"on\"}";
        }

    }

    @RequestMapping(value = "/getUserInfo", method = {RequestMethod.POST})
    @ResponseBody
    public String getUserInfo(UserInfo userInfo) throws SQLException {
        String sql = "SELECT * FROM USER_BASE_INFO WHERE USERID = " + userInfo.getUserID();
        ResultSet rs = stmt.executeQuery(sql);
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setUserID(rs.getString("USERID"));
        userBaseInfo.setName(rs.getString("NAME"));
        userBaseInfo.setEamil(rs.getString("EMAIL"));
        userBaseInfo.setNativeLanguage(rs.getString("NLANGUAGE"));
        userBaseInfo.setStudyLanguage(rs.getString("SLANGUAGE"));
        userBaseInfo.setLanguageLevel(rs.getString("LEVEL"));
        userBaseInfo.setInterest(rs.getString("INTEREST"));
        userBaseInfo.setToGoal(rs.getString("TOGOAL"));
        userBaseInfo.setToPerson(rs.getString("TOPERSON"));
        String json = JSON.toJSONString(userBaseInfo);
        return json;
    }

    @RequestMapping(value = "/setQuestion", method = {RequestMethod.POST})
    @ResponseBody
    public String setQuestion(Question question) throws SQLException {
        String sql = "INSERT INTO USER_QUESTION (USERID,NAME,QUESTIONTITLE,QUESTIONNUMBER,QUESTIONDETAIL,QUESTIONTIME,PICTURE,COUNTRY)" +
                "VALUES ('" + question.getUserID() + "','" + question.getName() + "','" + question.getQuestionTitle() + "','" + question.getQuestionNumber() + "','" +
                question.getQuestionDetail() + "','" + question.getQuestionTime() + "','" + question.getPicture() + "','" + question.getCountry() + "');";
        stmt.executeUpdate(sql);
        return "{\"status\":\"ok\"}";
    }

    @RequestMapping(value = "/getQuestion", method = {RequestMethod.POST})
    @ResponseBody
    public String getQuestion() throws SQLException {
        String sql = "SELECT * FROM USER_QUESTION";
        ResultSet rs = stmt.executeQuery(sql);
        ReturnQuestion returnQuestion = new ReturnQuestion();
        while (rs.next()) {
            Question question = new Question();
            question.setUserID(rs.getString("USERID"));
            question.setName(rs.getString("NAME"));
            question.setCountry(rs.getString("COUNTRY"));
            question.setPicture(rs.getString("PICTURE"));
            question.setQuestionNumber(rs.getInt("QUESTIONNUMBER"));
            question.setQuestionTitle(rs.getString("QUESTIONTITLE"));
            question.setQuestionDetail(rs.getString("QUESTIONDETAIL"));
            question.setQuestionTime(rs.getString("QUESTIONTIME"));
            returnQuestion.add(question);
        }
        String json = JSON.toJSONString(returnQuestion);
        return json;

    }


    @RequestMapping(value = "/setComment", method = {RequestMethod.POST})
    @ResponseBody
    public String setComment(SetComment setComment) throws SQLException {
        String sql = "INSERT INTO USER_COMMENT (QUESTIONNUMBER,USERID,COMMENTDETAIL,COMMENTTIME)" +
                "VALUES (" + setComment.getQuestionNumber() + ",'" + setComment.getUserID() + "','" + setComment.getCommentDetail() + "','" + setComment.getCommentTime() + "');";
        stmt.executeUpdate(sql);
        return "{\"status\":\"ok\"}";
    }

    @RequestMapping(value = "/getComment", method = {RequestMethod.POST})
    @ResponseBody
    public String Comment(Comment comment) throws SQLException {
        String sql = "SELECT * FROM USER_COMMENT WHERE QUESTIONNUMBER = " + comment.getQuestionNumber();
        ResultSet rs = stmt.executeQuery(sql);
        ReturnComment returnComment = new ReturnComment();
        while (rs.next()){
            System.out.println(rs.getString("USERID"));
            GetComment getComment = new GetComment();
            getComment.setUserID(rs.getString("USERID"));
            getComment.setQuestionNumber(comment.getQuestionNumber());
            String sql_base_info = "SELECT * FROM USER_BASE_INFO WHERE USERID = " + getComment.getUserID();
            ResultSet rs2 = stmt.executeQuery(sql_base_info);
            getComment.setName(rs2.getString("NAME"));
            getComment.setPicture(rs2.getString("PICURL"));
            String sql_comment = "SELECT * FROM USER_COMMENT WHERE USERID = " + getComment.getUserID();
            ResultSet rs3 = stmt.executeQuery(sql_comment);
            getComment.setCommentDetail(rs3.getString("COMMENTDETAIL"));
            getComment.setCommentTime(rs3.getString("COMMENTTIME"));
            returnComment.add(getComment);
            rs2.close();
            rs3.close();
        }
        rs.close();
        String json = JSON.toJSONString(returnComment);
        return json;
    }

    @RequestMapping(value = "/setTeacher", method = {RequestMethod.POST})
    @ResponseBody
    public String setTeacher(SetTeacher setTeacher) throws SQLException {
        String sql = "INSERT INTO CHAT_LIST (TEACHERID, NAME, PICURL, SCORE, LANGUAGE, INTRODUCELINK, EXPERIENCE, TYPE, PLAIN)" +
                "VALUES ('" + setTeacher.getTeacherID() + "','" + setTeacher.getName() + "','" + setTeacher.getPicurl() + "','" +
                setTeacher.getScore() + "','" + setTeacher.getLanguage() + "','" + setTeacher.getIntroduceLink() + "','" +
                setTeacher.getExperience() + "','" + setTeacher.getType() + "','" + setTeacher.getPlain() + "');";
        stmt.executeUpdate(sql);
        return "{\"status\":\"ok\"}";
    }

    @RequestMapping(value = "/getTeacher", method = {RequestMethod.POST})
    @ResponseBody
    public String getTeacher() throws SQLException {
        String sql = "SELECT * FROM CHAT_LIST";
        ResultSet rs = stmt.executeQuery(sql);
        GetTeacher getTeacher = new GetTeacher();
        while (rs.next()){
            SetTeacher setTeacher = new SetTeacher();
            setTeacher.setTeacherID(rs.getString("TEACHERID"));
            setTeacher.setName(rs.getString("NAME"));
            setTeacher.setPicurl(rs.getString("PICURL"));
            setTeacher.setScore(rs.getString("SCORE"));
            setTeacher.setLanguage(rs.getString("LANGUAGE"));
            setTeacher.setIntroduceLink(rs.getString("INTRODUCELINK"));
            setTeacher.setExperience(rs.getString("EXPERIENCE"));
            setTeacher.setType(rs.getString("TYPE"));
            setTeacher.setPlain(rs.getString("PLAIN"));
            getTeacher.add(setTeacher);
        }
        String json = JSON.toJSONString(getTeacher);
        return json;

    }



    public static void main(String[] args) {
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
        c = sqLiteJDBC.getC();
        stmt = sqLiteJDBC.getStmt();
        SpringApplication.run(Application.class, args);
    }
}
