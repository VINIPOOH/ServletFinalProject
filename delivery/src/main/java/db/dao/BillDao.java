package db.dao;

public interface BillDao {
    boolean createBill(long costInCents, long deliveriId, long UserId);
}
