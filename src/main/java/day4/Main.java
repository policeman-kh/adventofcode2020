package day4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    public void execute() {
        BufferedReader br = null;
        int count = 0;
        String tmp = "";
        try {
            InputStream in = getClass().getClassLoader()
                                       .getResourceAsStream("day4_part2.txt");
            br = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                tmp += line + " ";
                if (line.length() == 0) {
                    Map<String, String> map = Arrays.asList(tmp.trim().split(" "))
                                                    .stream()
                                                    .filter(array -> array.split(":").length == 2)
                                                    .collect(Collectors.toMap(k -> k.split(":")[0],
                                                                              v -> v.split(":")[1]));
                    if (new Password(map.get("byr"),
                                     map.get("iyr"),
                                     map.get("eyr"),
                                     map.get("hgt"),
                                     map.get("hcl"),
                                     map.get("ecl"),
                                     map.get("pid")).isValid()) {
                        count++;
                    }
                    tmp = "";
                }
            }
            System.out.println(count);
        } catch (Throwable e) {
            System.out.println(tmp);
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Password {
        String byr;
        String iyr;
        String eyr;
        String hgt;
        String hcl;
        String ecl;
        String pid;
        // Optional
        //String cid;

        public Password(String byr, String iyr, String eyr, String hgt, String hcl, String ecl,
                        String pid) {
            this.byr = byr;
            this.iyr = iyr;
            this.eyr = eyr;
            this.hgt = hgt;
            this.hcl = hcl;
            this.ecl = ecl;
            this.pid = pid;
        }

        public boolean isValid() {
            return isValidByr()
                   && isValidIyr()
                   && isValidEyr()
                   && isValidHgt()
                   && isValidHcl()
                   && isValidEcl()
                   && isValidPid();
        }

        private boolean isValidByr() {
            if(Strings.isNullOrEmpty(byr)){
                return false;
            }
            try {
                int year = Integer.parseInt(byr);
                return year >= 1920 && year <= 2002;
            } catch (Exception e) {
                return false;
            }
        }

        private boolean isValidIyr() {
            if(Strings.isNullOrEmpty(iyr)){
                return false;
            }
            try {
                int year = Integer.parseInt(iyr);
                return year >= 2010 && year <= 2020;
            } catch (Exception e) {
                return false;
            }
        }

        private boolean isValidEyr() {
            if(Strings.isNullOrEmpty(eyr)){
                return false;
            }
            try {
                int year = Integer.parseInt(eyr);
                return year >= 2020 && year <= 2030;
            } catch (Exception e) {
                return false;
            }
        }

        Pattern HGT_PATTERN = Pattern.compile("^([0-9]+)(cm|in)$");

        private boolean isValidHgt() {
            if(Strings.isNullOrEmpty(hgt)){
                return false;
            }
            Matcher matcher = HGT_PATTERN.matcher(hgt);
            if (matcher.matches()) {
                int val = Integer.parseInt(matcher.group(1));
                if (matcher.group(2).equals("cm")) {
                    return val >= 150 && val <= 193;
                } else {
                    return val >= 59 && val <= 76;
                }
            }
            return false;
        }

        Pattern HCL_PATTERN = Pattern.compile("^#[0-9a-f]{6}$");

        private boolean isValidHcl() {
            if(Strings.isNullOrEmpty(hcl)){
                return false;
            }
            Matcher matcher = HCL_PATTERN.matcher(hcl);
            return matcher.matches();
        }

        Pattern ECL_PATTERN = Pattern.compile("^(amb|blu|brn|gry|grn|hzl|oth)$");

        private boolean isValidEcl() {
            if(Strings.isNullOrEmpty(ecl)){
                return false;
            }
            return ECL_PATTERN.matcher(ecl).matches();
        }

        Pattern PID_PATTERN = Pattern.compile("^[0-9]{9}$");

        private boolean isValidPid() {
            if(Strings.isNullOrEmpty(pid)){
                return false;
            }
            return PID_PATTERN.matcher(pid).matches();
        }
    }
}
