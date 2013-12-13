package com.gdkdemo.sensor.environmental.cp.core;

import java.util.UUID;


// GUID object.
// 8-4-4-4-12
public final class GUID
{
    public final static GUID NULL = new GUID("00000000-0000-0000-0000-000000000000");
    public final static GUID ONE = new GUID("00000000-0000-0000-0000-000000000001");

    // Note that it is an implementation detail that we currently rely on UUID.
    private String guid;  // String representation of an UUID object, for now.

    public GUID()
    {
        // type 4 UUID, for now.
        guid = UUID.randomUUID().toString();
    }
    private GUID(String guid) throws IllegalArgumentException
    {
        if(guid != null && guid.length() == 22) {
            this.guid = GUID.convertFromShortString(guid);
        } else {
            this.guid = UUID.fromString(guid).toString();
        }
    }

    public static GUID create()
    {
        return new GUID();
    }

    public static String generate()
    {
        //return (new GUID()).toString();
        return UUID.randomUUID().toString();
    }

    public static String generateShortString()
    {
        return (new GUID()).toShortString();
    }

    public static GUID fromString(String guid) throws IllegalArgumentException 
    {
        if(Log.D) Log.d("GUID.fromString() called with guid = " + guid);
        return new GUID(UUID.fromString(guid).toString());
    }


    public static GUID fromShortString(String str) throws IllegalArgumentException 
    {
        String guid = GUID.convertFromShortString(str);
        return new GUID(UUID.fromString(guid).toString());
    }
    
    public static boolean isValid(String guid)
    {
        try {
            UUID.fromString(guid);
        } catch(Exception ex) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean equals(Object rhs)
    {
        if(rhs instanceof GUID) {
            return guid.equals(rhs.toString());
        } else if(rhs instanceof String) {
            return guid.equals(rhs);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return guid.hashCode();
    }

    @Override
    public String toString()
    {
        return guid;
    }
    
    // Returns 22 char string, comprising [A-Za-z0-9-_]
    public String toShortString()
    {
        return GUID.convertToShortString(this.guid);
    }

//    @Override
//    protected Object clone() throws CloneNotSupportedException
//    {
//        throw new CloneNotSupportedException("GUID cannot be cloned.");
//    }
 
    public static String convertFromShortString(String str) throws IllegalArgumentException
    {
        // 22 char string -> guid 8-4-4-4-12.
        if(str == null || str.length() != 22) {
            throw new IllegalArgumentException();
        }

        StringBuffer sb = new StringBuffer();
        for(int i=0; i<22; i+=2) {
            String part = str.substring(i, i+2);
            int base = reverseBase64Char(part.charAt(0)) * 64 + reverseBase64Char(part.charAt(1));
            String s3 = Integer.toHexString(base);
            if(s3.length() == 1) {
                s3 = "00" + s3;
            } else if(s3.length() == 2) {
                s3 = "0" + s3;
            }
            if(i == 0) {
                sb.append(s3.substring(1));
            } else if(i == 4) {
                sb.append(s3);
                sb.append('-');
            } else if(i == 8) {
                sb.append(s3.substring(0,1));
                sb.append('-');
                sb.append(s3.substring(1));
            } else if(i == 10) {
                sb.append(s3.substring(0,2));
                sb.append('-');
                sb.append(s3.substring(2));
            } else if(i == 12) {
                sb.append(s3);
                sb.append('-');
            } else {
                sb.append(s3);
            }
        }
        return sb.toString();
    }
    
    // Returns 22 char string, comprising [A-Za-z0-9-_]
    public static String convertToShortString(String guid)
    {
        // Guid format: 8-4-4-4-12.
        if(guid == null || guid.length() == 0) {
            return guid;  // Error
        }
        String padded = guid;
        if(guid.length() <= 36) {
            // TBD: pad it with zeros. (Just add one zero in front for now.)
            padded = "0" + guid;
        }
        int totalLen = padded.length();

        StringBuffer sb = new StringBuffer();
        int idx = 0;
        while (idx < totalLen) {
            StringBuffer part = new StringBuffer();
            int cnt = 0;
            while(cnt < 3 && idx < totalLen) {
                char c;
                if((c = padded.charAt(idx++)) != '-') {
                    part.append(c);
                    cnt++;
                }
            }
            int base = Integer.parseInt(part.toString(), 16);  // 3 digit number
            StringBuffer str2 = new StringBuffer();
            for(int j=0; j<2; j++) {
                int tmp = base / 64;
                int rem = base - tmp * 64;
                base = tmp;
                str2.insert(0, getBase64Char(rem));            // 2 char string
            }
            sb.append(str2);
        }        

        return sb.toString();
    }

    private static char getBase64Char(int i)
    {
        char c;
        if(i >= 0 && i < 26) {
            c = (char) (i + 65);        // A == 65
        } else if(i >= 26 && i < 52) {
            c = (char) (i - 26 + 97);   // a == 97
        } else if(i >= 52 && i < 62) {
            c = (char) (i - 52 + 48);   // 0 == 48
        } else { // 62 <= i < 64
            if(i == 62) {
                c = '-';
            } else {
                c = '_';
            }
        }
        return c;
    }

    // Char: [A-Za-z0-9-_]
    private static int reverseBase64Char(char c)
    {
        int i;
        if(c >= 97 && c < 123) {
            i = (c - 97) + 26;
        } else if(c >= 65 && c < 91) {
            i = (c - 65) + 0;
        } else {  // c < 65 || c >= 123
            if(c >= 48 && c < 58) {
                i = (c - 48) + 52;
            } else if(c == '_') {  // ascii 95
                i = 63;
            } else if(c == '-') {  // ascii 45
                i = 62;
            } else {
                i = 0;  // This should not happen.
            }
        }
        return i;
    }
    
    // temporary
    //public static List<String> parseGUIDs(String guids)
    public static String[] parseGUIDs(String guids)
    {
        if(guids == null || guids.length() == 0) {
            return null;
        }
        String[] guidArr = guids.split(",");
        // TBD: trim element.
        // TBD: check if each element is a valid guid
        // TBD: ...
        //List<String> guidList = Arrays.asList(guidArr);
        //return guidList;
        return guidArr;
    }
    
}
