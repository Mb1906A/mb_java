
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class Invoice {

    public ArrayList<InvoiceInfo> invoiceInfoList;

    public Invoice() {
        invoiceInfoList = new ArrayList<InvoiceInfo>();
    }

    public void GAZTDefaultValues() {

        invoiceInfoList.add(new InvoiceInfo((byte) 1, "محمد"));
        invoiceInfoList.add(new InvoiceInfo((byte) 2, "310122393500003"));
        invoiceInfoList.add(new InvoiceInfo((byte) 3, "2022-04-25T15:30:00Z"));
        invoiceInfoList.add(new InvoiceInfo((byte) 4, "1000.00"));
        invoiceInfoList.add(new InvoiceInfo((byte) 5, "150.00"));

    }

    public String GenerateHexString() {
        String hex = "";
        if (CheckTagInfoList(invoiceInfoList) == false) {
            return hex;
        }
        for (InvoiceInfo invoiceInfo : invoiceInfoList) {
            hex += GetTLVInHexString(invoiceInfo);
        }
        return hex;
    }

    public String GenerateBase64String() {
        if (GenerateHexString() == null) {
            return null;
        }
        String originalInput = ConvertHexToString(GenerateHexString());
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
        return encodedString;
    }

    private boolean CheckTagInfoList(ArrayList<InvoiceInfo> invoiceInfoList) {
        if (invoiceInfoList == null || invoiceInfoList.size() < 5) {
            return false;
        }
        for (InvoiceInfo invoiceInfo : invoiceInfoList) {
            if (invoiceInfo.TagId == 0 || GetLengthOfTagValue(invoiceInfo.TageValue) > 255) {
                return false;
            }
        }
        return true;
    }

    private int GetLengthOfTagValue(String tagValue) {

        try {
            byte[] bytes = tagValue.getBytes("UTF-8");
            return bytes.length;
        } catch (Exception e) {
            return 256;
        }

    }

    private String GetTLVInHexString(InvoiceInfo invoiceInfo) {
        String hex = GetIntInHexString(invoiceInfo.TagId)
                + GetIntInHexString(GetLengthOfTagValue(invoiceInfo.TageValue))
                + GetTagValueInHexString(invoiceInfo.TageValue);
        return hex;
    }

    private String GetIntInHexString(int intValue) {

        return String.format("%02X", intValue);
    }

    private String GetTagValueInHexString(String value) {
        StringBuilder stringBuilder = new StringBuilder();

        char[] charArray = value.toCharArray();

        for (char c : charArray) {
            String charToHex = Integer.toHexString(c);
            stringBuilder.append(charToHex);
        }

        return stringBuilder.toString();
    }

    private String ConvertHexToString(String hexString) {
        if (hexString == null) {
            return null;
        }
        int l = hexString.length();
        byte[] data = new byte[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        byte[] bytes = data;
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
