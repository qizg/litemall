package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.ThirdPayHistoryMapper;
import org.linlinjava.litemall.db.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThirdPayHistoryService {

    @Resource
    private ThirdPayHistoryMapper historyMapper;

    public void add(ThirdPayHistory history) {
        history.setAddTime(LocalDateTime.now());
        history.setUpdateTime(LocalDateTime.now());
        historyMapper.insertSelective(history);
    }

    public int updateById(ThirdPayHistory history) {
        history.setUpdateTime(LocalDateTime.now());
        return historyMapper.updateByPrimaryKeySelective(history);
    }

    public List<ThirdPayHistory> querySelective(Integer userId, Integer page, Integer size, String sort, String order) {
        ThirdPayHistoryExample example = new ThirdPayHistoryExample();
        ThirdPayHistoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(userId);
        }

        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return historyMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        historyMapper.logicalDeleteByPrimaryKey(id);
    }
}
