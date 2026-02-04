# Спецификация языка JUnior (черновик)

> Статус: черновик. Документ фиксирует базовые правила языка.

## 1. Лексика
- **Идентификаторы**: `[a-zA-Z_][a-zA-Z0-9_]*`
- **Числа**: только целые (на старте), например `0`, `42`, `1234`.
- **Пробелы**: пробелы, табы, переводы строки разделяют токены.
- **Комментарии**: `//` до конца строки.

## 2. Типы
- `int` (знаковое 64-битное целое в эталонной реализации)
- `bool` (`true`/`false`)
- `string` (UTF-8, неизменяемая)

## 3. Выражения
- Арифметика: `+`, `-`, `*`, `/` (целочисленное деление)
- Сравнения: `==`, `!=`, `<`, `<=`, `>`, `>=`
- Логика: `&&`, `||`, `!`

## 4. Операторы
- Объявление переменной: `let name = expr;`
- Присваивание: `name = expr;`
- Печать: `print(expr);`
- Блок: `{ stmt* }`

## 5. Управление потоком
- `if (cond) { ... } else { ... }`
- `while (cond) { ... }`

## 6. Пример программы
```junior
let x = 5;
let y = 7;
if (x < y) {
  print(x + y);
} else {
  print(x - y);
}
```

## 7. Черновик расширенного примера (по описанию пользователя)
Ниже сохранён фрагмент пользовательского синтаксиса как референс для дальнейшей
формализации грамматики и семантики. Он **не является** частью стабильной
спецификации и может измениться.

```junior
workspace(new.workplace()){
  namespace(new.nameplace()){
    name:'test'
    format:'.junior'
  }
  diskspace(new.diskplace()){
    Base:"$HOME/test/"
  }
  dataspace(new.dataplace()){
    lib(new.liberplace()){
      Stand_lib:"junior::utilman::util:: \"uril::standart\""
    }
    exit(new.(exit_code)place()){
      EXIT_CODE_OKK = (0)
      EXIT_CODE_ERR = (-0)
      EXIT_CODE_NONE = (HEX(3F) || 10c.(63))
    }
  }
  codespace(new.codeplace($USER.code)){
    my.lib(<<< Stand_lib)
    my.exit(<<< EXIT_CODE_*)

    class RecursiveTraversalNumbers for Iclass{
      new.array_massive[new.nums][1][2][3][4][5][6][7]
        massive.nums = new.val[1] = [10]
        massive.nums = new.val[2] = [HEX(#CC)]
        massive.nums = new.val[3] = [10]
        massive.nums = new.val[4] = [01]
        massive.nums = new.val[5] = [65]
        massive.nums = new.val[6] = [0]
        massive.nums = new.val[7] = [2]

    //*@(non-base)Constructor::

      using mt::math <<< my.lib(Stand_lib)
      math = use.massive.nums:: all
      all ::nums [+]
      System.out.printk(nums) >>> consolas

      using fn recurs for massive.nums
      if null{
        should massive_nums.length(-1) = |.length(1)
        recurs::
          |.leght(-1) = 1
          |.leght(-2) = 2
          |.leght(-3) = 3
          |.leght(-4) = 4
          |.leght(-5) = 5
          |.leght(-6) = 6
          |.leght(-7) = 7
      }
      @(non-base)Constructor::
        massive_nums >>> new.new.massive_nums([<<< recurs])
      System.out.printk(new.nums) >>> consolas
    }
    class Exit for Iclass{
      if code_complite
        to EXIT_CODE_OKK
      else if code_err
        to EXIT_CODE_ERR
      else EXIT_CODE_NONE
    }
    return EXIT_CODE_* <<< cl::Exit
  }
}
```

## 8. Обязательная библиотека (черновик)
На старте интерпретатор должен подгружать базовую библиотеку `uril` с классом
`standart`, которая включает:
- базовые типы (`int`, `bool`, `string`);
- функции вывода (`print`, `println`);
- соглашения по кодам выхода (в примере `EXIT_CODE_*`);
- минимальный набор утилит для работы со строками и числами.
