package jcn.jclan.utilities;

public class PluginVocab {
    // Префикс плагина
    public static final String PLUGIN_PREFIX = "[JClan]";

    // Разрешения для участников и создателя клана
    public static final String CLAN_MEMBER_PERMISSION = "clan.member";
    public static final String CLAN_CREATOR_PERMISSION = "clan.creator";

    // Сообщения об успешном принятии заявки и удалении клана
    public static final String ACCEPT_CLAN_INVITE_MESSAGE = " Вы успешно приняли заявку в клан ";
    public static final String CLAN_ACCEPT_DELETE_MESSAGE = " Клан удален";

    // Сообщение об ошибке при неизвестной команде
    public static final String UNKNOWN_CLAN_COMMAND = " Неизвестная команда! Для просмотра всех команд используйте: /clan help!";

    // Использование команды приглашения в клан
    public static final String USAGE_CLAN_INVITE_COMMAND = " Использование: /clan invite (ник игрока)";

    // Список всех команд плагина
    public static final String LIST_OF_ALL_PLUGIN_COMMANDS = " Список всех команд плагина JClan для ";

    // Сообщение об удалении из клана после удаления клана
    public static final String CLAN_KICK_MESSAGE_AFTER_DELETE = " Вы были удалены из клана, так как создатель удалил его";

    // Сообщения об отклонении заявки и оставлении клана
    public static final String CLAN_DECLINE_INVITE_MESSAGE = " Вы отклонили заявку на вступление в ";
    public static final String CLAN_DECLINE_DELETE_MESSAGE = " Вы решили оставить клан";

    // Сообщение об ошибке неизвестной страницы
    public static final String UNKNOWN_PAGE_MESSAGE = " Ошибка! Неизвестная страница";

    // Сообщение со списком всех кланов
    public static final String LIST_OF_ALL_CLANS_MESSAGE = " Список всех кланов (Страница %s / %s )";

    // Сообщение о подтверждении удаления клана
    public static final String DO_YOU_WANT_DELETE_CLAN = " Вы действительно хотите удалить клан %s?";

    // Сообщение об ошибке отсутствия прав для создания клана
    public static final String DO_NOT_HAVE_PERMISSION_TO_CREATE_CLAN = " Ошибка! У вас недостаточно прав для создания кланов. Обратитесь в тикет, если хотите создать клан.";

    // Сообщения об ошибках при создании клана
    public static final String WE_ALREADY_IN_CLAN_ERROR = " Ошибка! Вы уже находитесь в клане.";
    public static final String NEED_MINIMUM_TWO_SYMBOLS_ERROR = " Ошибка! Название клана должно содержать хотя бы 2 символа.";
    public static final String CLAN_NAME_ALREADY_USE_ERROR = " Ошибка! Клан с таким названием уже существует.";

    // Сообщение об успешном создании клана
    public static final String CLAN_CREATE_SUCCESSFUL_MESSAGE = " Клан успешно создан!";

    // Сообщения с информацией о клане
    public static final String CLAN_NAME_IS_MESSAGE = " Название клана: ";
    public static final String CLAN_PREFIX_IS_MESSAGE = " Префикс клана: ";

    // Команды для управления кланом
    public static final String COMMAND_LEAVE = "/clan leave - Команда для выхода из клана.";
    public static final String COMMAND_CREATE = "/clan create - Команда для создания клана.";
    public static final String COMMAND_INVITE = "/clan invite - Команда для приглашения игрока в клан.";
    public static final String COMMAND_KICK = "/clan kick - Команда для удаления игрока из клана.";
    public static final String COMMAND_GUI = "/clan gui - Меню клана.";
    public static final String COMMAND_LIST = "/clan list - Список всех кланов.";
    public static final String COMMAND_DELETE = "/clan delete - Удаление своего клана.";

    // Сообщения об ошибках при приглашении игрока
    public static final String YOU_NEED_TO_BE_CLAN_CREATOR_TO_INVITE_ERROR = " Вы должны находиться в клане и быть его главой, чтобы приглашать игроков";
    public static final String PLAYER_NOT_FOUND_ERROR = " Игрок с именем %s не найден или не в сети.";
    public static final String PLAYER_IN_ANOTHER_CLAN = " Игрок с именем %s уже состоит в другом клане.";

    // Сообщения об успешном приглашении и кнопках для подтверждения/отклонения
    public static final String SEND_INVITE_PLAYER = " Вы отправили приглашение %s ";
    public static final String ACCEPT = "Принять";
    public static final String DECLINE = "Отклонить";
    public static final String INVITE_IN_CLAN = " Вас пригласили в клан %s";

    // Сообщения об ошибках при исключении игрока
    public static final String YOU_NEED_TO_BE_CLAN_CREATOR_TO_KICK_ERROR = " Вы должны находиться в клане и быть его главой, чтобы исключать игроков";
    public static final String YOU_CAN_NOT_KICK_YOURSELF = " Вы не можете исключить самого себя";
    public static final String PLAYER_IS_NOT_IN_YOUR_CLAN = " Игрок с именем %s не является участником вашего клана.";

    // Сообщения о невозможности покинуть клан и об успешном выходе
    public static final String YOU_CAN_NOT_LEAVE_YOUR_CLAN = " Вы не можете покинуть свой клан, только удалить его";
    public static final String LEFT_THE_CLAN = " Вы покинули клан";

    // Сообщение об отсутствии участия в клане
    public static final String YOU_DO_NOT_IN_CLAN = " Вы не находитесь в клане";

    // Кнопки для подтверждения/отмены при удалении клана
    public static final String DELETE = "Удалить";
    public static final String KEEP = "Оставить";

    // Сообщение об успешном исключении игрока из клана
    public static final String KICK_PLAYER_FROM_CLAN = " Игрок с именем %s успешно исключен из клана.";

    // Разделительная линия для форматирования вывода
    public static final String LINE_SEPARATOR = "---------------------------------------";

    // Сообщения для GuiCommand
    public static final String GUI_MESSAGE_NO_PERMISSION = " Вы должны быть участником клана, чтобы открыть меню клана!";
    public static final String GUI_MESSAGE_INFO_TITLE = "Меню клана";
    public static final String GUI_MESSAGE_INFO_TITLE_SETTING = "Настройки клана";
    public static final String GUI_MESSAGE_NAME_BUTTON = "Информация";
    public static final String GUI_MESSAGE_STATISTIC_BUTTON = "Участники";
    public static final String GUI_MESSAGE_SETTING_BUTTON = "Настройки";
    public static final String GUI_MESSAGE_MEMBERS_TITLE_PREFIX = "Участники клана ";
    public static final String GUI_MESSAGE_MEMBERS_BUTTON = "Нажмите, чтобы посмотреть список";
    public static final String GUI_MESSAGE_SETTING_NAME_TAG = "Префикс клана";
    public static final String GUI_MESSAGE_SETTING_BOOK = "Название клана";
    public static final String GUI_MESSAGE_SETTING_CHANGE_NAME = " Новое название клана: ";
    public static final String GUI_MESSAGE_SETTING_CHANGE_PREFIX = " Новый префикс клана: ";
    public static final String GUI_MESSAGE_SETTING_ONLY_LEADER_NAME = " Изменять названия клана может только глава клана";
    public static final String GUI_MESSAGE_SETTING_ONLY_LEADER_PREFIX = " Изменять префикс клана может только глава клана";
    public static final String GUI_MESSAGE_SETTING_PREFIX_LENGTH_ERROR = " Префикс должен состоять из 2х символов";
    public static final String NAME = "Название: ";
    public static final String PREFIX = "Префикс: ";
    public static final String CREATOR = "Создатель: ";
    public static final String MEMBERS = "Участники: ";
    public static final String CLICK_TO_OPEN_CLAN_SETTINGS = "Нажмите, чтобы открыть настройки клана";
    public static final String CLICK_TO_CHANGE_CLAN_NAME = "Нажмите, чтобы изменить название клана";
    public static final String CLICK_TO_CHANGE_CLAN_PREFIX = "Нажмите, чтобы изменить префикс клана";
    public static final String TRY_AGAIN = "Попробуйте еще раз";
}
