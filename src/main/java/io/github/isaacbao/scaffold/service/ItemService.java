package io.github.isaacbao.scaffold.service;

import io.github.isaacbao.scaffold.domain.entity.Item;
import io.github.isaacbao.scaffold.domain.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 和物品相关的service
 * Created by rongyang_lu on 2017/7/10.
 */
public interface ItemService {

    void addItem(User user, Map<String, String> pm);

    void updateItem(User user, Map<String, String> pm);

    List<Item> searchItem(User user, Map<String, String> pm);

    Item getItem(User user, String id);
}
