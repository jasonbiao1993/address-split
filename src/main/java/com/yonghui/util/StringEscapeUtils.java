package com.yonghui.util;

import org.apache.commons.lang3.text.translate.*;

/**
 * @author jasonbiao
 * @date 2020-09-03 10:55
 * description: <p>
 *
 * </p>
 */
public class StringEscapeUtils {

    private static final String[][] ISO8859_1_ESCAPE = new String[][]{{" ", "&nbsp;"}, {"¡", "&iexcl;"}, {"¢", "&cent;"}, {"£", "&pound;"}, {"¤", "&curren;"}, {"¥", "&yen;"}, {"¦", "&brvbar;"}, {"§", "&sect;"}, {"¨", "&uml;"}, {"©", "&copy;"}, {"ª", "&ordf;"}, {"«", "&laquo;"}, {"¬", "&not;"}, {"\u00ad", "&shy;"}, {"®", "&reg;"}, {"¯", "&macr;"}, {"°", "&deg;"}, {"±", "&plusmn;"}, {"²", "&sup2;"}, {"³", "&sup3;"}, {"´", "&acute;"}, {"µ", "&micro;"}, {"¶", "&para;"}, {"", "&middot;"}, {"¸", "&cedil;"}, {"¹", "&sup1;"}, {"º", "&ordm;"}, {"»", "&raquo;"}, {"¼", "&frac14;"}, {"½", "&frac12;"}, {"¾", "&frac34;"}, {"¿", "&iquest;"}, {"À", "&Agrave;"}, {"Á", "&Aacute;"}, {"Â", "&Acirc;"}, {"Ã", "&Atilde;"}, {"Ä", "&Auml;"}, {"Å", "&Aring;"}, {"Æ", "&AElig;"}, {"Ç", "&Ccedil;"}, {"È", "&Egrave;"}, {"É", "&Eacute;"}, {"Ê", "&Ecirc;"}, {"Ë", "&Euml;"}, {"Ì", "&Igrave;"}, {"Í", "&Iacute;"}, {"Î", "&Icirc;"}, {"Ï", "&Iuml;"}, {"Ð", "&ETH;"}, {"Ñ", "&Ntilde;"}, {"Ò", "&Ograve;"}, {"Ó", "&Oacute;"}, {"Ô", "&Ocirc;"}, {"Õ", "&Otilde;"}, {"Ö", "&Ouml;"}, {"×", "&times;"}, {"Ø", "&Oslash;"}, {"Ù", "&Ugrave;"}, {"Ú", "&Uacute;"}, {"Û", "&Ucirc;"}, {"Ü", "&Uuml;"}, {"Ý", "&Yacute;"}, {"Þ", "&THORN;"}, {"ß", "&szlig;"}, {"à", "&agrave;"}, {"á", "&aacute;"}, {"â", "&acirc;"}, {"ã", "&atilde;"}, {"ä", "&auml;"}, {"å", "&aring;"}, {"æ", "&aelig;"}, {"ç", "&ccedil;"}, {"è", "&egrave;"}, {"é", "&eacute;"}, {"ê", "&ecirc;"}, {"ë", "&euml;"}, {"ì", "&igrave;"}, {"í", "&iacute;"}, {"î", "&icirc;"}, {"ï", "&iuml;"}, {"ð", "&eth;"}, {"ñ", "&ntilde;"}, {"ò", "&ograve;"}, {"ó", "&oacute;"}, {"ô", "&ocirc;"}, {"õ", "&otilde;"}, {"ö", "&ouml;"}, {"÷", "&divide;"}, {"ø", "&oslash;"}, {"ù", "&ugrave;"}, {"ú", "&uacute;"}, {"û", "&ucirc;"}, {"ü", "&uuml;"}, {"ý", "&yacute;"}, {"þ", "&thorn;"}, {"ÿ", "&yuml;"}};
    private static final String[][] ISO8859_1_UNESCAPE;

    public static final CharSequenceTranslator UNESCAPE_HTML4;

    public static final String unescapeHtml4(String input) {
        return UNESCAPE_HTML4.translate(input);
    }

    public static String[][] invert(String[][] array) {
        String[][] newarray = new String[array.length][2];

        for(int i = 0; i < array.length; ++i) {
            newarray[i][0] = array[i][1];
            newarray[i][1] = array[i][0];
        }

        return newarray;
    }

    public static String[][] ISO8859_1_UNESCAPE() {
        return (String[][])ISO8859_1_UNESCAPE.clone();
    }

    static {
        ISO8859_1_UNESCAPE = invert(ISO8859_1_ESCAPE);
        UNESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(ISO8859_1_UNESCAPE()), new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
    }

}
