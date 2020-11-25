package daniel.varga.tacocloud.repositories;

import daniel.varga.tacocloud.domain.Order;

public interface OrderRepository {

    public Order save(Order order);
}
