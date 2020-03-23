import service.GoodReadsRequestService;

public class GoodReadsTester {

    public static void main(final String[] args) {
        final GoodReadsRequestService requestService = new GoodReadsRequestService();

        requestService.getReadShelve();
    }

}