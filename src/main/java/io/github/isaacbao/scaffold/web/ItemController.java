package io.github.isaacbao.scaffold.web;

import io.github.isaacbao.scaffold.domain.base.bean.ResponseInfo;
import io.github.isaacbao.scaffold.pretreatment.CommonPretreatment;
import io.github.isaacbao.scaffold.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 和物品相关的controller
 * Created by rongyang_lu on 2017/7/4.
 */
@RestController
@RequestMapping(value = "/item")
@Api(value = "item", description = "和“物品”相关的接口")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CommonPretreatment commonPretreatment;

    /**
     * 查询所有物品
     *
     */
    @ApiOperation(value = "查询物品", notes = "根据条件查询物品，返回列表", response = ResponseInfo.class,consumes="application/xml;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "页数", required = true, dataType = "Integer",paramType =
                    "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, dataType = "Integer",paramType =
                    "form")
    })
    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseInfo searchitem(HttpServletRequest request) {
        return commonPretreatment.pretreat(request, (req, user, pm) -> itemService.searchItem(user, pm));
    }

    /**
     * 查看单条物品的详细信息
     */
    @ApiOperation(value = "查看物品", notes = "查看单条物品的详细信息", response = ResponseInfo.class,consumes="application/xml;charset=UTF-8")
    @ApiImplicitParam(name = "id", value = "物品信息的ID", required = true, dataType = "String",paramType =
            "form")
    @RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseInfo getitem(HttpServletRequest request, String id) {
        return commonPretreatment.pretreat(request, (req, user, pm) -> itemService.getItem(user, id));
    }

    /**
     * 新增物品
     *
     */
    @ApiOperation(value = "新增物品",consumes="application/xml;charset=UTF-8")
    @ApiImplicitParam(name = "item", value = "物品信息的ID", required = true, dataType = "item",paramType =
            "form")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseInfo additem(HttpServletRequest request) {
        return commonPretreatment.pretreat(request, (req, user, pm) -> {
            itemService.addItem(user, pm);
            return "";
        });
    }

    /**
     * 编辑物品
     *
     */
    @ApiOperation(value = "编辑物品", notes = "变更某个物品的信息",consumes="application/xml;charset=UTF-8")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseInfo updateitem(HttpServletRequest request) throws Exception {
        return commonPretreatment.pretreat(request, (req, user, pm) -> {
            itemService.updateItem(user, pm);
            return "";
        });
    }
}
