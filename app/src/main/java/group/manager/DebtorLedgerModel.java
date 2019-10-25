package group.manager;

/**
 * Created by intel on 12-06-2017.
 */

public class DebtorLedgerModel {
    public String tvdate,tvdesc,tvdebit,tvcredit,tvamt;

    public DebtorLedgerModel(String tvdate,String tvdesc,String tvdebit,String tvcredit,String tvamt)
    {
        this.tvdate = tvdate;
        this.tvdesc = tvdesc;
        this.tvdebit = tvdebit;
        this.tvcredit=tvcredit;
        this.tvamt=tvamt;

    }
}
