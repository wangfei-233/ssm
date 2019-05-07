package legion.service;
import legion.entity.Goods;
import legion.entity.GoodsFlow;

import java.util.ArrayList;
import java.util.List;

public interface GoodsService {
     Integer addGoods(Goods goods);

     Integer updateGoods(Goods goods);

     Integer deleteGoods(Integer id);

     Goods listGoodsById(Integer Goodsid);

     ArrayList<Goods> listGoods();

     ArrayList<Goods>listGoodsByType(Integer type);

     Integer addGoodsflow(Integer goodsid,Integer num, String date,String admin,String descs );

     ArrayList<GoodsFlow> listGoodsflow(Integer page);
}
