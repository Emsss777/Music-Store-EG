package app.web.controller;

import app.web.util.PageBuilder;
import app.model.enums.PrimaryGenre;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static app.util.UrlPaths.URL_ADMIN_ALBUMS;
import static app.util.Views.VIEW_ADMIN_ALBUMS;

@Controller
@RequiredArgsConstructor
@SuppressWarnings("JvmTaintAnalysis")
public class AlbumController {

    private final PageBuilder pageBuilder;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(URL_ADMIN_ALBUMS)
    public ModelAndView getAdminAlbumsPage(@RequestParam(required = false) PrimaryGenre genre) {

        return pageBuilder.buildAlbumsPage(VIEW_ADMIN_ALBUMS, genre);
    }
}
