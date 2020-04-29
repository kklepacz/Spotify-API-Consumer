package pl.connectis.spotifyapicli.APICalls;

import java.util.List;

public interface ApiCaller<T> {
    List<T> getList(String ids);
}
