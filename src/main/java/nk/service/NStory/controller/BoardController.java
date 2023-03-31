package nk.service.NStory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {

    @RequestMapping(value = "/whiteboard")
    public String boardList() {
        return "WhiteBoard";
    }

    @GetMapping(value = "/whiteboard/add")
    public String AddBoard() {
        return "WhiteBoardAdd";
    }

    @GetMapping(value = "/whiteboard/view")
    public String BoardView() {
        return "WhiteBoardView";
    }
}
