package in.tech_camp.protospace_b.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RankingController {

    private final PrototypeShowRepository prototypeShowRepository;

    // ランキングページに遷移
    @GetMapping("/prototypes/ranking")
    public String showRanking(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

        // ログインユーザーのID取得
        Integer userId = (currentUser != null) ? currentUser.getId() : null;

        // いいね数順に並び替えたプロトタイプを取得
        List<PrototypeEntity> prototypes = prototypeShowRepository.findPrototypesOrderByCountDesc(userId);

        model.addAttribute("prototypes", prototypes);
        

        return "prototype/ranking";
    }
}
