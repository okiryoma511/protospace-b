package in.tech_camp.protospace_b.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.PrototypeSearchForm;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TopPageController {

    private final PrototypeShowRepository prototypeShowRepository;
    private final UserDetailRepository userDetailRepository;

    @GetMapping("")
    public String topPage(@AuthenticationPrincipal CustomUserDetail currentUser,
        @RequestParam(name = "sort", defaultValue = "desc") String sort, Model model) {

        // ログイン時のみuserIdを取得
        Integer userId = (currentUser != null) ? currentUser.getId() : null;
        model.addAttribute("user", userDetailRepository.findById(userId));

        // 並び順に応じてプロトタイプを取得
        List<PrototypeEntity> prototypes = "asc".equalsIgnoreCase(sort)
            ? prototypeShowRepository.showAllOrderByCreatedAtAsc(userId)
            : prototypeShowRepository.showAll(userId);

        // 公開プロトタイプだけを抽出
        List<PrototypeEntity> publishedPrototypes = prototypes.stream()
            .filter(PrototypeEntity::isPublished)
            .collect(Collectors.toList());

        // モデルに追加
        model.addAttribute("prototypes", publishedPrototypes);
        model.addAttribute("sort", sort);

        // プロトタイプ検索フォームをモデルに渡す
        PrototypeSearchForm prototypeSearchForm = new PrototypeSearchForm();
        model.addAttribute("prototypeSearchForm", prototypeSearchForm);

        return "index";
    }
}
