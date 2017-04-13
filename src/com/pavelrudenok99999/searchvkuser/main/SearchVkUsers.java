package com.pavelrudenok99999.searchvkuser.main;

import com.pavelrudenok99999.searchvkuser.util.HtmlGetter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

/**
 * Created by Pavel on 05.03.2017.
 */

public class SearchVkUsers {
    private static final String API_VK_URL = "https://api.vk.com";
    private static final int OFFSET = 1000;
    private static final int COUNT = 1000;

    private List<String> groups = new ArrayList<>();

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Bad arguments");
            return;
        }

        String pathToFile = args[0];
        File file = new File(pathToFile);

        System.out.println(new SearchVkUsers(file).searchUsers());
    }

    public SearchVkUsers(File groupsFile) {
        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(groupsFile);
            br = new BufferedReader(fr);
            String group;

            while ((group = br.readLine()) != null) {
                groups.add(group);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }

                if (br != null) {
                    br.close();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public SearchVkUsers(List<String> groups) {
        this.groups = groups;
    }

    public Set<String> searchUsers() {
        ArrayList<Set<String>> allUsers = new ArrayList<>();

        for (String group : groups) {
            allUsers.add(getUsersByGroup(group));
        }

        Set<String> users = allUsers.get(0);

        for (int i = 1; i < allUsers.size(); i++) {
            users.retainAll(allUsers.get(i));
        }

        return users;
    }

    private Set<String> getUsersByGroup(String group) {
        Set<String> usersSet = new HashSet<>();
        Group gr = getGroup(group);

        if (gr == null) return usersSet;

        long countMembers = gr.getMembersCount();
        int factorOffset = 0;

        do {
            String membersHtml = HtmlGetter.getByUrl(
                    API_VK_URL + "/method/groups.getMembers?group_id=" +
                            gr.getId() + "&count=" + COUNT + "&offset=" + factorOffset * OFFSET);

            usersSet.addAll(parseUsersInGroup(membersHtml));

        } while (countMembers > ++factorOffset * OFFSET);

        return usersSet;
    }

    public Set<String> parseUsersInGroup(String html) {
        Set<String> set = new HashSet<>();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject)parser.parse(html);
            jsonObj = (JSONObject)jsonObj.get("response");

            JSONArray arr = (JSONArray)jsonObj.get("users");

            for (Object s: arr) {
                set.add(s.toString());
            }

        } catch  (ParseException e) {
            e.printStackTrace();
        }

        return set;
    }

    public static Group parseGroup(String html) {
        JSONObject jsonObj = null;
        JSONArray arr = null;

        try {
            JSONParser parser = new JSONParser();
            jsonObj = (JSONObject)parser.parse(html);
            arr = (JSONArray)jsonObj.get("response");

        } catch  (ParseException e) {
            e.printStackTrace();
        }

        if (arr == null) return null;

        jsonObj = (JSONObject) arr.get(0);

        if (jsonObj.get("gid") == null || jsonObj.get("name") == null || jsonObj.get("members_count") == null) {
            return null;
        }

        return new Group((long)jsonObj.get("gid"), (String)jsonObj.get("name"), (long)jsonObj.get("members_count"));
    }

    public static Group getGroup(String group) {
        String groupHtml = HtmlGetter.getByUrl(API_VK_URL + "/method/groups.getById?group_id=" +
                group + "&fields=members_count");

        return parseGroup(groupHtml);
    }
}