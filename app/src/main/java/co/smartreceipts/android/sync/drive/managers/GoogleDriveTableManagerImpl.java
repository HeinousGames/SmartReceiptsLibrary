package co.smartreceipts.android.sync.drive.managers;

import androidx.annotation.NonNull;

import com.google.common.base.Preconditions;

import javax.inject.Inject;

import co.smartreceipts.automatic_backups.drive.managers.DriveDatabaseManager;
import co.smartreceipts.core.di.scopes.ApplicationScope;
import co.smartreceipts.android.model.Category;
import co.smartreceipts.android.model.Column;
import co.smartreceipts.android.model.Distance;
import co.smartreceipts.android.model.PaymentMethod;
import co.smartreceipts.android.model.Receipt;
import co.smartreceipts.android.model.Trip;
import co.smartreceipts.android.persistence.database.controllers.impl.CSVTableController;
import co.smartreceipts.android.persistence.database.controllers.impl.CategoriesTableController;
import co.smartreceipts.android.persistence.database.controllers.impl.DistanceTableController;
import co.smartreceipts.android.persistence.database.controllers.impl.PDFTableController;
import co.smartreceipts.android.persistence.database.controllers.impl.PaymentMethodsTableController;
import co.smartreceipts.android.persistence.database.controllers.impl.ReceiptTableController;
import co.smartreceipts.android.persistence.database.controllers.impl.TripTableController;
import co.smartreceipts.android.sync.drive.listeners.DatabaseBackupListener;
import co.smartreceipts.android.sync.drive.listeners.ReceiptBackupListener;

@ApplicationScope
public class GoogleDriveTableManagerImpl implements GoogleDriveTableManager {

    private final TripTableController tripTableController;
    private final ReceiptTableController receiptTableController;
    private final CategoriesTableController categoriesTableController;
    private final CSVTableController csvTableController;
    private final PDFTableController pdfTableController;
    private final PaymentMethodsTableController paymentMethodsTableController;
    private final DistanceTableController distanceTableController;

    private DatabaseBackupListener<Trip> tripDatabaseBackupListener;
    private ReceiptBackupListener receiptDatabaseBackupListener;
    private DatabaseBackupListener<Distance> distanceDatabaseBackupListener;
    private DatabaseBackupListener<PaymentMethod> paymentMethodDatabaseBackupListener;
    private DatabaseBackupListener<Category> categoryDatabaseBackupListener;
    private DatabaseBackupListener<Column<Receipt>> csvColumnDatabaseBackupListener;
    private DatabaseBackupListener<Column<Receipt>> pdfColumnDatabaseBackupListener;

    @Inject
    public GoogleDriveTableManagerImpl(@NonNull TripTableController tripTableController,
                                       @NonNull ReceiptTableController receiptTableController,
                                       @NonNull CategoriesTableController categoriesTableController,
                                       @NonNull CSVTableController csvTableController,
                                       @NonNull PDFTableController pdfTableController,
                                       @NonNull PaymentMethodsTableController paymentMethodsTableController,
                                       @NonNull DistanceTableController distanceTableController) {
        this.tripTableController = Preconditions.checkNotNull(tripTableController);
        this.receiptTableController = Preconditions.checkNotNull(receiptTableController);
        this.categoriesTableController = Preconditions.checkNotNull(categoriesTableController);
        this.csvTableController = Preconditions.checkNotNull(csvTableController);
        this.pdfTableController = Preconditions.checkNotNull(pdfTableController);
        this.paymentMethodsTableController = Preconditions.checkNotNull(paymentMethodsTableController);
        this.distanceTableController = Preconditions.checkNotNull(distanceTableController);
    }

    @Override
    public void initializeListeners(@NonNull DriveDatabaseManager driveDatabaseManager,
                                    @NonNull DriveReceiptsManager driveReceiptsManager) {

        this.tripDatabaseBackupListener = new DatabaseBackupListener<>(driveDatabaseManager);
        this.receiptDatabaseBackupListener = new ReceiptBackupListener(driveDatabaseManager, driveReceiptsManager);
        this.distanceDatabaseBackupListener = new DatabaseBackupListener<>(driveDatabaseManager);
        this.paymentMethodDatabaseBackupListener = new DatabaseBackupListener<>(driveDatabaseManager);
        this.categoryDatabaseBackupListener = new DatabaseBackupListener<>(driveDatabaseManager);
        this.csvColumnDatabaseBackupListener = new DatabaseBackupListener<>(driveDatabaseManager);
        this.pdfColumnDatabaseBackupListener = new DatabaseBackupListener<>(driveDatabaseManager);

        tripTableController.subscribe(tripDatabaseBackupListener);
        receiptTableController.subscribe(receiptDatabaseBackupListener);
        distanceTableController.subscribe(distanceDatabaseBackupListener);
        paymentMethodsTableController.subscribe(paymentMethodDatabaseBackupListener);
        categoriesTableController.subscribe(categoryDatabaseBackupListener);
        csvTableController.subscribe(csvColumnDatabaseBackupListener);
        pdfTableController.subscribe(pdfColumnDatabaseBackupListener);
    }

    @Override
    public void deinitializeListeners() {
        tripTableController.unsubscribe(tripDatabaseBackupListener);
        receiptTableController.unsubscribe(receiptDatabaseBackupListener);
        distanceTableController.unsubscribe(distanceDatabaseBackupListener);
        paymentMethodsTableController.unsubscribe(paymentMethodDatabaseBackupListener);
        categoriesTableController.unsubscribe(categoryDatabaseBackupListener);
        csvTableController.unsubscribe(csvColumnDatabaseBackupListener);
        pdfTableController.unsubscribe(pdfColumnDatabaseBackupListener);
    }
}
