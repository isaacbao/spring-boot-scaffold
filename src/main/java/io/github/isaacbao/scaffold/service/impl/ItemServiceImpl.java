package io.github.isaacbao.scaffold.service.impl;

import com.google.common.collect.Lists;
import io.github.isaacbao.scaffold.dao.ItemRepository;
import io.github.isaacbao.scaffold.domain.entity.Item;
import io.github.isaacbao.scaffold.domain.entity.User;
import io.github.isaacbao.scaffold.service.ItemService;
import io.github.isaacbao.scaffold.util.FieldSetter;
import io.github.isaacbao.scaffold.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 物品相关的service implement
 * Created by rongyang_lu on 2017/7/10.
 */
@Service
public class ItemServiceImpl implements ItemService {

    private static Logger logger = LogManager.getLogger();

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void addItem(User user, Map<String, String> pm) {
        Item item = new Item();
        FieldSetter.setFields(item, pm);
        itemRepository.save(item);
    }

    @Override
    public void updateItem(User user, Map<String, String> pm) {
        String id = pm.get("id");
        Item item = itemRepository.findById(UUID.fromString(id)).get();
        FieldSetter.setFields(item, pm);
        itemRepository.save(item);
    }

    @Override
    public List<Item> searchItem(User user, Map<String, String> pm) {
        Iterable<Item> items = itemRepository.findAll();
        return Lists.newArrayList(items);
    }

    @Override
    public Item getItem(User user, String id) {
//        item item = itemRepository.findById(UUID.fromString(id)).get();
        Item item = JsonUtils.readValue("2333", Item.class);

        return item;
    }
}
