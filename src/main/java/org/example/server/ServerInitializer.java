package org.example.server;

import org.example.common.commands.Info;

/**
 * Фабрика инициализации сервера: создаёт CommandManager и регистрирует все доступные команды.
 * Используется при запуске ServerApp для настройки обработчика запросов.
 */
public class ServerInitializer {

    /**
     * Создаёт и настраивает CommandManager со всеми командами.
     * @return готовый к использованию CommandManager
     */
    public static Applecation createCommandManager() {
        Applecation cmdManager = new Applecation();

        HelpCommand helpCmd = new HelpCommand(cmdManager);
        cmdManager.register(helpCmd);
        cmdManager.register(new Info());
        cmdManager.register(new ShowCommand());
        cmdManager.register(new ExitCommand());
        cmdManager.register(new AddCommand());
        cmdManager.register(new UpdateCommand());
        cmdManager.register(new InsertAtCommand());      // вставка по индексу
        cmdManager.register(new AddIfMaxCommand());      // добавить, если элемент "больше" текущего максимума
        cmdManager.register(new RemoveByIdCommand());    // удаление по ID
        cmdManager.register(new ClearCommand());         // очистить коллекцию
        cmdManager.register(new SaveCommand());          // сохранить в файл
        cmdManager.register(new SortCommand());                          // сортировка по полю (например, area)
        cmdManager.register(new CountLessThanStandardOfLivingCommand()); // подсчёт элементов < заданного уровня
        cmdManager.register(new FilterByGovernorCommand());              // фильтрация по губернатору
        cmdManager.register(new PrintFieldAscendingStandardOfLivingCommand()); // вывод поля в порядке возрастания


        ExecuteScriptCommand executeScriptCmd = new ExecuteScriptCommand(cmdManager);
        cmdManager.register(executeScriptCmd);

        System.out.println("✅ Зарегистрировано команд: " + cmdManager.getRegisteredCount());

        return cmdManager;
    }


}