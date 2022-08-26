package com.ericlam.mc.mvvm;

import com.ericlam.mc.eld.BukkitManagerProvider;
import com.ericlam.mc.eld.ELDBukkit;
import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.mvvm.examples.multichat.ChatViewModel;
import com.ericlam.mc.mvvm.examples.tictaetoe.TicTaeToeViewModel;
import com.ericlam.mc.mvvm.services.MultiChatService;
import com.ericlam.mc.mvvm.services.MultiChatServiceImpl;
import com.ericlam.mc.mvvm.services.TicTaeToeService;
import com.ericlam.mc.mvvm.services.TicTaeToeServiceImpl;
import org.eldependenci.mvvm.MVVMInstallation;


@ELDBukkit(
        registry = ExampleRegistries.class,
        lifeCycle = ExampleLifeCycle.class
)
public final class ExamplesPlugin extends ELDBukkitPlugin {
    @Override
    protected void manageProvider(BukkitManagerProvider provider) {

    }

    @Override
    public void bindServices(ServiceCollection serviceCollection) {
        MVVMInstallation installer = serviceCollection.getInstallation(MVVMInstallation.class);
        installer.bindId("multichat", ChatViewModel.class);
        installer.bindId("tictaetoe", TicTaeToeViewModel.class);

        serviceCollection.bindService(MultiChatService.class, MultiChatServiceImpl.class);
        serviceCollection.bindService(TicTaeToeService.class, TicTaeToeServiceImpl.class);
    }
}
