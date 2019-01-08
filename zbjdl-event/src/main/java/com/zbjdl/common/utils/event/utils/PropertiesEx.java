//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

public class PropertiesEx extends Properties {
    private static final long serialVersionUID = 1844482790810427085L;

    public PropertiesEx() {
    }

    public synchronized void load(Reader reader) throws IOException {
        this.load0(new PropertiesEx.LineReader(reader));
    }

    public synchronized void load(InputStream inStream) throws IOException {
        this.load0(new PropertiesEx.LineReader(inStream));
    }

    private void load0(PropertiesEx.LineReader lr) throws IOException {
        char[] convtBuf = new char[1024];

        int limit;
        while((limit = lr.readLine()) >= 0) {
            int keyLen = 0;
            int valueStart = limit;
            boolean hasSep = false;

            char c;
            for(boolean precedingBackslash = false; keyLen < limit; ++keyLen) {
                c = lr.lineBuf[keyLen];
                if (c == '=' && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    hasSep = true;
                    break;
                }

                if ((c == ' ' || c == '\t' || c == '\f') && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    break;
                }

                if (c == '\\') {
                    precedingBackslash = !precedingBackslash;
                } else {
                    precedingBackslash = false;
                }
            }

            for(; valueStart < limit; ++valueStart) {
                c = lr.lineBuf[valueStart];
                if (c != ' ' && c != '\t' && c != '\f') {
                    if (hasSep || c != '=' && c != ':') {
                        break;
                    }

                    hasSep = true;
                }
            }

            String key = this.loadConvert(lr.lineBuf, 0, keyLen, convtBuf);
            String value = this.loadConvert(lr.lineBuf, valueStart, limit - valueStart, convtBuf);
            this.put(key, value);
        }

    }

    private String loadConvert(char[] in, int off, int len, char[] convtBuf) {
        if (convtBuf.length < len) {
            int newLen = len * 2;
            if (newLen < 0) {
                newLen = 2147483647;
            }

            convtBuf = new char[newLen];
        }

        char[] out = convtBuf;
        int outLen = 0;
        int end = off + len;

        while(true) {
            while(true) {
                while(off < end) {
                    char aChar = in[off++];
                    if (aChar == '\\') {
                        aChar = in[off++];
                        if (aChar == 'u') {
                            int value = 0;

                            for(int i = 0; i < 4; ++i) {
                                aChar = in[off++];
                                switch(aChar) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    value = (value << 4) + aChar - 48;
                                    break;
                                case ':':
                                case ';':
                                case '<':
                                case '=':
                                case '>':
                                case '?':
                                case '@':
                                case 'G':
                                case 'H':
                                case 'I':
                                case 'J':
                                case 'K':
                                case 'L':
                                case 'M':
                                case 'N':
                                case 'O':
                                case 'P':
                                case 'Q':
                                case 'R':
                                case 'S':
                                case 'T':
                                case 'U':
                                case 'V':
                                case 'W':
                                case 'X':
                                case 'Y':
                                case 'Z':
                                case '[':
                                case '\\':
                                case ']':
                                case '^':
                                case '_':
                                case '`':
                                default:
                                    throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                    value = (value << 4) + 10 + aChar - 65;
                                    break;
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                    value = (value << 4) + 10 + aChar - 97;
                                }
                            }

                            out[outLen++] = (char)value;
                        } else {
                            if (aChar == 't') {
                                aChar = '\t';
                            } else if (aChar == 'r') {
                                aChar = '\r';
                            } else if (aChar == 'n') {
                                aChar = '\n';
                            } else if (aChar == 'f') {
                                aChar = '\f';
                            }

                            out[outLen++] = aChar;
                        }
                    } else {
                        out[outLen++] = aChar;
                    }
                }

                return new String(out, 0, outLen);
            }
        }
    }

    class LineReader {
        byte[] inByteBuf;
        char[] inCharBuf;
        char[] lineBuf = new char[1024];
        int inLimit = 0;
        int inOff = 0;
        InputStream inStream;
        Reader reader;

        public LineReader(InputStream inStream) {
            this.inStream = inStream;
            this.inByteBuf = new byte[8192];
        }

        public LineReader(Reader reader) {
            this.reader = reader;
            this.inCharBuf = new char[8192];
        }

        int readLine() throws IOException {
            int len = 0;
            boolean skipWhiteSpace = true;
            boolean isCommentLine = false;
            boolean isNewLine = true;
            boolean appendedLineBegin = false;
            boolean precedingBackslash = false;
            boolean skipLF = false;

            while(true) {
                while(true) {
                    char c;
                    while(true) {
                        do {
                            if (this.inOff >= this.inLimit) {
                                this.inLimit = this.inStream == null ? this.reader.read(this.inCharBuf) : this.inStream.read(this.inByteBuf);
                                this.inOff = 0;
                                if (this.inLimit <= 0) {
                                    if (len != 0 && !isCommentLine) {
                                        return len;
                                    }

                                    return -1;
                                }
                            }

                            if (this.inStream != null) {
                                c = (char)(255 & this.inByteBuf[this.inOff++]);
                            } else {
                                c = this.inCharBuf[this.inOff++];
                            }

                            if (!skipLF) {
                                break;
                            }

                            skipLF = false;
                        } while(c == '\n');

                        if (!skipWhiteSpace) {
                            break;
                        }

                        if (c != ' ' && c != '\t' && c != '\f' && (appendedLineBegin || c != '\r' && c != '\n')) {
                            skipWhiteSpace = false;
                            appendedLineBegin = false;
                            break;
                        }
                    }

                    if (isNewLine) {
                        isNewLine = false;
                        if (c == '#' || c == '!') {
                            isCommentLine = true;
                            continue;
                        }
                    }

                    if (c != '\n' && c != '\r') {
                        this.lineBuf[len++] = c;
                        if (len == this.lineBuf.length) {
                            int newLength = this.lineBuf.length * 2;
                            if (newLength < 0) {
                                newLength = 2147483647;
                            }

                            char[] buf = new char[newLength];
                            System.arraycopy(this.lineBuf, 0, buf, 0, this.lineBuf.length);
                            this.lineBuf = buf;
                        }

                        if (c == '\\') {
                            precedingBackslash = !precedingBackslash;
                        } else {
                            precedingBackslash = false;
                        }
                    } else if (!isCommentLine && len != 0) {
                        if (this.inOff >= this.inLimit) {
                            this.inLimit = this.inStream == null ? this.reader.read(this.inCharBuf) : this.inStream.read(this.inByteBuf);
                            this.inOff = 0;
                            if (this.inLimit <= 0) {
                                return len;
                            }
                        }

                        if (!precedingBackslash) {
                            return len;
                        }

                        --len;
                        skipWhiteSpace = true;
                        appendedLineBegin = true;
                        precedingBackslash = false;
                        if (c == '\r') {
                            skipLF = true;
                        }
                    } else {
                        isCommentLine = false;
                        isNewLine = true;
                        skipWhiteSpace = true;
                        len = 0;
                    }
                }
            }
        }
    }
}
