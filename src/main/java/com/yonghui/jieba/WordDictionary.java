package com.yonghui.jieba;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Slf4j
public class WordDictionary {
    private static WordDictionary singleton;
    private static final String MAIN_DICT = "/dict.txt";
    private static final String USER_DICT_SUFFIX = ".dict";

    public final Map<String, Double> freqs = new HashMap<String, Double>();
    public final Set<String> loadedPath = new HashSet<String>();
    private Double minFreq = Double.MAX_VALUE;
    private Double total = 0.0;
    private DictSegment _dict;


    private WordDictionary() {
        this.loadDict();
    }


    public static WordDictionary getInstance() {
        if (singleton == null) {
            synchronized (WordDictionary.class) {
                if (singleton == null) {
                    singleton = new WordDictionary();
                    return singleton;
                }
            }
        }
        return singleton;
    }


    /**
     * for ES to initialize the user dictionary.
     * 
     */
    public void init() {
        synchronized (WordDictionary.class) {
            URL resource = getClass().getResource("/dict");
            Path abspath = Paths.get(resource.getPath());
            try(DirectoryStream<Path> stream = Files.newDirectoryStream(abspath, String.format(Locale.getDefault(), "*%s", USER_DICT_SUFFIX))) {
                for (Path path: stream){
                    log.info(String.format(Locale.getDefault(), "loading dict %s", path.toString()));
                    singleton.loadUserDict(path);
                }
                loadedPath.add(abspath.toString());
            } catch (IOException e) {
                log.error(String.format(Locale.getDefault(), "%s: load user dict failure!", abspath.toString()), e);
            }
        }
    }
    
    public void init(String[] paths) {
        synchronized (WordDictionary.class) {
            for (String path: paths){
                if (!loadedPath.contains(path)) {
                    try {
                        log.info("initialize user dictionary: " + path);
                        singleton.loadUserDict(path);
                        loadedPath.add(path);
                    } catch (Exception e) {
                        log.error(String.format(Locale.getDefault(), "%s: load user dict failure!", path), e);
                    }
                }
            }
        }
    }
    
    /**
     * let user just use their own dict instead of the default dict
     */
    public void resetDict(){
    	_dict = new DictSegment((char) 0);
    	freqs.clear();
    }


    public void loadDict() {
        _dict = new DictSegment((char) 0);

        try(
                InputStream is = this.getClass().getResourceAsStream(MAIN_DICT);
                BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))
        ){

            long s = System.currentTimeMillis();
            while (br.ready()) {
                String line = br.readLine();
                String[] tokens = line.split("[\t ]+");

                if (tokens.length < 2) {
                    continue;
                }

                String word = tokens[0];
                double freq = Double.valueOf(tokens[1]);
                total += freq;
                word = addWord(word);
                freqs.put(word, freq);
            }
            // normalize
            for (Entry<String, Double> entry : freqs.entrySet()) {
                entry.setValue((Math.log(entry.getValue() / total)));
                minFreq = Math.min(entry.getValue(), minFreq);
            }
            log.info(String.format(Locale.getDefault(), "main dict load finished, time elapsed %d ms",
                System.currentTimeMillis() - s));
        }
        catch (IOException e) {
            log.error(String.format(Locale.getDefault(), "%s load failure!", MAIN_DICT), e);
        }
    }


    private String addWord(String word) {
        if (null != word && !"".equals(word.trim())) {
            String key = word.trim().toUpperCase(Locale.getDefault());
            _dict.fillSegment(key.toCharArray());
            return key;
        }
        else {
            return null;
        }
    }


    public void loadUserDict(Path userDict) {
        loadUserDict(userDict, StandardCharsets.UTF_8);
    }

    public void loadUserDict(String userDictPath) {
        loadUserDict(userDictPath, StandardCharsets.UTF_8);
    }
    
    public void loadUserDict(Path userDict, Charset charset) {                
        try(BufferedReader br = Files.newBufferedReader(userDict, charset)) {
            long s = System.currentTimeMillis();
            int count = 0;
            while (br.ready()) {
                String line = br.readLine();
                String[] tokens = line.split("[\t ]+");

                if (tokens.length < 1) {
                    // Ignore empty line
                    continue;
                }

                String word = tokens[0];

                double freq = 3.0d;
                if (tokens.length == 2)
                    freq = Double.valueOf(tokens[1]);
                word = addWord(word); 
                freqs.put(word, Math.log(freq / total));
                count++;
            }
            log.info(String.format(Locale.getDefault(), "user dict %s load finished, tot words:%d, time elapsed:%dms", userDict.toString(), count, System.currentTimeMillis() - s));
        } catch (IOException e) {
            log.error(String.format(Locale.getDefault(), "%s: load user dict failure!", userDict.toString()), e);
        }
    }

    public void loadUserDict(String userDictPath, Charset charset) {

        try(
                InputStream is = this.getClass().getResourceAsStream(userDictPath);
                BufferedReader br = new BufferedReader(new InputStreamReader(is, charset))
                ) {
            long s = System.currentTimeMillis();
            int count = 0;
            while (br.ready()) {
                String line = br.readLine();
                String[] tokens = line.split("[\t ]+");

                if (tokens.length < 1) {
                    // Ignore empty line
                    continue;
                }

                String word = tokens[0];

                double freq = 3.0d;
                if (tokens.length == 2)
                    freq = Double.valueOf(tokens[1]);
                word = addWord(word);
                freqs.put(word, Math.log(freq / total));
                count++;
            }
            log.info(String.format(Locale.getDefault(), "user dict %s load finished, tot words:%d, time elapsed:%dms", userDictPath, count, System.currentTimeMillis() - s));
        } catch (IOException e) {
            log.error(String.format(Locale.getDefault(), "%s: load user dict failure!", userDictPath), e);
        }
    }
    
    public DictSegment getTrie() {
        return this._dict;
    }


    public boolean containsWord(String word) {
        return freqs.containsKey(word);
    }


    public Double getFreq(String key) {
        if (containsWord(key)) {
            return freqs.get(key);
        } else {
            return minFreq;
        }
    }
}
