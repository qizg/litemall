package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.UserBalanceHistoryMapper;
import org.linlinjava.litemall.db.domain.UserBalanceHistory;
import org.linlinjava.litemall.db.domain.UserBalanceHistoryExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserBalanceHistoryService {

    @Resource
    private UserBalanceHistoryMapper balanceMapper;

    public void add(UserBalanceHistory history) {
        history.setAddTime(LocalDateTime.now());
        history.setUpdateTime(LocalDateTime.now());
        balanceMapper.insertSelective(history);
    }

    public int updateById(UserBalanceHistory history) {
        history.setUpdateTime(LocalDateTime.now());
        return balanceMapper.updateByPrimaryKeySelective(history);
    }

    public List<UserBalanceHistory> querySelective(Integer userId, Integer page, Integer size, String sort, String order) {
        UserBalanceHistoryExample example = new UserBalanceHistoryExample();
        UserBalanceHistoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(userId);
        }

        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return balanceMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        balanceMapper.logicalDeleteByPrimaryKey(id);
    }
}
