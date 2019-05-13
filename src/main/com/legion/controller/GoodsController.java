package legion.controller;

import com.alibaba.fastjson.JSONObject;
import legion.entity.Goods;

import legion.entity.GoodsApply;
import legion.entity.GoodsFlow;
import legion.entity.ProGoods;
import legion.service.GoodsService;

import legion.service.ProjectService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import javax.annotation.Resource;


@CrossOrigin
@RestController
//@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @Resource
    private ProjectService projectService;


    @RequestMapping(value = "/goods/{id}",method = RequestMethod.GET)
    public ArrayList goodsbyid(@PathVariable Integer id){
        ArrayList<Goods> list = new ArrayList<>();
            Goods goods = goodsService.listGoodsById(id);
                list.add(goods);
                return list;
    }

    @RequestMapping(value = "/goods/type/{type}",method = RequestMethod.GET)
    public ArrayList goodsbytype(@PathVariable Integer type){
        ArrayList<Goods> list = new ArrayList<>();
        list = goodsService.listGoodsByType(type);
        return list;
    }

    @RequestMapping(value = "/goodsflow",method = RequestMethod.GET)
    public ArrayList goodsflow(@RequestParam(value = "page")Integer page){
        int nowpage = (page-1)*10;
        ArrayList<GoodsFlow> list = goodsService.listGoodsflow(nowpage);
        return  list;
    }

    @RequestMapping(value = "/goods",method = RequestMethod.GET)
    public ArrayList listgood(){
        ArrayList<Goods> list = goodsService.listGoods();
        return list;
    }

    @RequestMapping(value = "/goodsapply",method = RequestMethod.GET)
    public JSONObject listgoodsapply(@RequestParam(value = "page")Integer page){
        JSONObject  obj = new JSONObject();
        obj.put("GoodsApply", goodsService.listGoodsApply( (page-1)*10));
        obj.put("length",goodsService.goodsapplynum());
        return obj;
    }

    @Transactional
    @RequestMapping(value = "/goodsapply",method = RequestMethod.PATCH)
    public JSONObject listgoodsapply(@RequestBody GoodsApply goodsApply){
        JSONObject  obj = new JSONObject();
        String nowstate =goodsApply.getState();
        if(nowstate =="通过"|| nowstate.equals("通过"))
        {
            if(projectService.updategoodsapply(goodsApply)==0){
                throw new RuntimeException();
            }
            ProGoods proGoods =projectService.findprogoods(goodsApply.getProgoodsid());
            proGoods.setActualnum(proGoods.getApplynum()+proGoods.getActualnum());
            proGoods.setApplynum(0);
            if(projectService.updateprogressgoods(proGoods)==0){
                throw new RuntimeException();
            }
            Goods goods = goodsService.listGoodsById(goodsApply.getGoodsid());
            goods.setNumber(goods.getNumber()-goodsApply.getApplynum().intValue());
            goods.setLatelynum(-goodsApply.getApplynum().intValue());
            goods.setLatelydate(goodsApply.getDate());
            if(goodsService.updateGoods(goods)==0){
                throw new RuntimeException();
            }
            if(goodsService.addGoodsflow(goods.getId(),goods.getLatelynum(),goods.getLatelydate(),
                    goodsApply.getAdmin(),goodsApply.getReason())==0){
                throw new RuntimeException();
            }
            obj.put("code",1);
            obj.put("result","审核结果通过");
        }
        else if(nowstate =="不通过" || nowstate.equals("不通过"))
        {
            if(projectService.updategoodsapply(goodsApply)==0)
            {
                throw new RuntimeException();
            }
            ProGoods proGoods =projectService.findprogoods(goodsApply.getProgoodsid());

            proGoods.setApplynum(0);
            if(projectService.updateprogressgoods(proGoods )==0){
                throw new RuntimeException();
            }
            obj.put("code",1);
            obj.put("result","审核结果不通过");
        }

        return obj;
    }

    @RequestMapping(value = "/goods",method = RequestMethod.POST)
    public Integer addgoods(@RequestBody Goods goods ){
        return goodsService.addGoods(goods);
    }

    @RequestMapping(value ="/goods",method = RequestMethod.PATCH)
    public Integer updategoods(@RequestBody Goods goods){
        return goodsService.updateGoods(goods);
    }

    @RequestMapping(value = "/goods/{id}",method = RequestMethod.DELETE)
    public Integer deletegoods(@PathVariable Integer id)
    {
        return goodsService.deleteGoods(id);
    }




}
