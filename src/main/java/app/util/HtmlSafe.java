package app.util;

import lombok.NoArgsConstructor;
import org.springframework.web.util.HtmlUtils;

import java.util.Optional;

@NoArgsConstructor
public final class HtmlSafe {

    public static String esc(String s) {

        return Optional.ofNullable(s)
                .map(HtmlUtils::htmlEscape)
                .orElse(null);
    }
}
