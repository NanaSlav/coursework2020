package ru.nanaslav.planner.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;

@Service
public class PaginationService {
    public void setPages(int page, int last, int size, Model model) {
        ArrayList<Integer> p = new ArrayList<>();
        if (page != 1) {
            p.add(1);
            if ((page - 3) > 2) {
                p.add(0);
                p.add(page - 3);
                p.add(page - 2);
                p.add(page - 1);
                p.add(page);
            } else {
                for(int cur = 2; cur <= page; cur++) {
                    p.add(cur);
                }
            }
        } else {
            p.add(1);
        }
        if (page != last) {
            if ((page + 1) <= last) {
                p.add(page + 1);
                if ((page + 2) <= last) {
                    p.add(page + 2);
                    if ((page + 3) <= last) {
                        p.add(page + 3);
                        if ((page + 3) < last) {
                            if ((page + 4) < last ) {
                                p.add(0);
                            }
                            p.add(last);
                        }
                    }
                }
            }
        }
        int prev = (page != 1) ? page - 1 : 1;
        int next = (page != last) ? page + 1 : last;
        model.addAttribute("size", size);
        model.addAttribute("prev", prev);
        model.addAttribute("next", next);
        model.addAttribute("pageable", p);
    }
}
