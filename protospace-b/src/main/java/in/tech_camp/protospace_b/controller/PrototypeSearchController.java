package in.tech_camp.protospace_b.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.TagEntity;
import in.tech_camp.protospace_b.form.PrototypeSearchForm;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.TagRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeSearchController {
    private final PrototypeShowRepository prototypeShowRepository;
    private final TagRepository tagRepository;

    @GetMapping("/prototypes/search")
    public String searchPrototypes(@ModelAttribute("prototypeSearchForm") PrototypeSearchForm prototypeSearchForm,
            @AuthenticationPrincipal CustomUserDetail currentUser,
            @RequestParam(value = "prototypeName", required = false) String prototypeName,
            @RequestParam(value = "tag", required = false) Integer tag_id, Model model) {
        String keyword = prototypeName;
        Integer tagId = tag_id;
        TagEntity tag = null;

        // ログインユーザーのID取得
        Integer userId = (currentUser != null) ? currentUser.getId() : null;
        List<PrototypeEntity> prototypes;

        if (keyword == null || keyword.trim().isEmpty()) {
            if (tagId == null) {
                // 入力が空ならリダイレクトして初期状態へ戻す
                return "redirect:/";
            } else {
                // 入力が空だがtagsがある場合
                prototypes = prototypeShowRepository.findByPrototypeNameWithTag(userId, "", tagId);
                tag = tagRepository.getById(tagId);
            }
        } else {
            if (tagId == null) {
                prototypes = prototypeShowRepository.findByPrototypeName(userId, keyword);
            } else {
                prototypes = prototypeShowRepository.findByPrototypeNameWithTag(userId, keyword, tagId);
                tag = tagRepository.getById(tagId);
            }
        }

        model.addAttribute("prototypes", prototypes);
        model.addAttribute("tag", tag);
        model.addAttribute("prototypeSearchForm", prototypeSearchForm);

        return "prototype/search";
    }
}