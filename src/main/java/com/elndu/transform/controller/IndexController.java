package com.elndu.transform.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author elndu
 * @version 1.0, 2020/9/18 17:19
 * @since JDK 1.8
 */
@RestController
public class IndexController {
    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @return org.springframework.web.servlet.ModelAndView
     * elndu  2020/9/21 - 9:39
     **/
    @RequestMapping("/transform")
    public ModelAndView transform()
    {
        return new ModelAndView("transform");
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>

     * @return org.springframework.web.servlet.ModelAndView
     * elndu  2020/9/21 - 14:30
     **/
    @RequestMapping("/merge")
    public ModelAndView merge()
    {
        return new ModelAndView("merge");
    }
}
