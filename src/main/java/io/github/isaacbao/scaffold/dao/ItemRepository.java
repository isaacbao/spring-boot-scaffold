package io.github.isaacbao.scaffold.dao;

import io.github.isaacbao.scaffold.domain.entity.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * 物品 DAO
 * Created by rongyang_lu on 2017/7/10.
 */
public interface ItemRepository extends CrudRepository<Item, UUID> {
}
