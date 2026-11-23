package com.example.jjamyechan;

import java.util.HashMap;

public class UserManager {

    // 가입한 유저들: 아이디 → User
    private static final HashMap<String, User> users = new HashMap<>();

    // 비밀번호: 아이디 → 비밀번호
    // (User 클래스에는 비밀번호를 넣지 않기 때문에 여기서 따로 보관. 서버 연동 후에는 삭제)
    private static final HashMap<String, String> passwords = new HashMap<>();

    // 지금 로그인한 유저. 로그인 전이거나 로그아웃했으면 null
    private static User currentUser = null;

    // 테스트용 계정이 필요하면 아래 주석을 풀어 쓰세요. (제출 전에는 다시 지우기)
    // 앱이 켜질 때 한 번 실행돼서, 매번 회원가입하지 않고 바로 로그인해 볼 수 있어요.
    //
     static {
         User test = new User("test", "테스트", "soldier", "private", "2026-01-01", "2027-07-01");
         users.put(test.getEmail(), test);
         passwords.put(test.getEmail(), "12345678");
     }

    // 회원가입
    public static boolean signup(User user, String password) {
        String email = user.getEmail();

        // 같은 아이디가 이미 있으면 가입 실패
        if (users.containsKey(email)) {
            return false;
        }

        users.put(email, user);
        passwords.put(email, password);

        // 가입한 사람은 바로 로그인된 상태로
        currentUser = user;
        return true;
    }

    // 로그인
    public static boolean login(String username, String password) {
        // 없는 아이디
        if (!users.containsKey(username)) {
            return false;
        }
        // 비밀번호 비교 (문자열은 == 가 아니라 equals 로 비교)
        String savedPassword = passwords.get(username);
        if (!password.equals(savedPassword)) {
            return false;
        }
        currentUser = users.get(username);
        return true;
    }

    // 로그아웃
    public static void logout() {
        currentUser = null;
    }
    public static void leaveUnit() {
        getCurrentUser().setUnitCode(null);
        getCurrentUser().setRole(null);
    }

    // 지금 로그인한 유저 리턴
    public static User getCurrentUser() {
        return currentUser;
    }
    // 지금 로그인인지
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
