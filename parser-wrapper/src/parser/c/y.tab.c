/* A Bison parser, made by GNU Bison 3.0.4.  */

/* Bison implementation for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015 Free Software Foundation, Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* C LALR(1) parser skeleton written by Richard Stallman, by
   simplifying the original so-called "semantic" parser.  */

/* All symbols defined below should begin with yy or YY, to avoid
   infringing on user name space.  This should be done even for local
   variables, as they might otherwise be expanded by user macros.
   There are some unavoidable exceptions within include files to
   define necessary library symbols; they are noted "INFRINGES ON
   USER NAME SPACE" below.  */

/* Identify Bison output.  */
#define YYBISON 1

/* Bison version.  */
#define YYBISON_VERSION "3.0.4"

/* Skeleton name.  */
#define YYSKELETON_NAME "yacc.c"

/* Pure parsers.  */
#define YYPURE 0

/* Push parsers.  */
#define YYPUSH 0

/* Pull parsers.  */
#define YYPULL 1




/* Copy the first part of user declarations.  */
#line 1 "C.y" /* yacc.c:339  */

#include "nodes.h"
#define YYSTYPE NODE*
#define YYDEBUG 1
extern TOKEN *int_token, *void_token, *function_token, *lasttok;
NODE *ans;

#line 74 "y.tab.c" /* yacc.c:339  */

# ifndef YY_NULLPTR
#  if defined __cplusplus && 201103L <= __cplusplus
#   define YY_NULLPTR nullptr
#  else
#   define YY_NULLPTR 0
#  endif
# endif

/* Enabling verbose error messages.  */
#ifdef YYERROR_VERBOSE
# undef YYERROR_VERBOSE
# define YYERROR_VERBOSE 1
#else
# define YYERROR_VERBOSE 0
#endif


/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token type.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    IDENTIFIER = 258,
    CONSTANT = 259,
    STRING_LITERAL = 260,
    LE_OP = 261,
    GE_OP = 262,
    EQ_OP = 263,
    NE_OP = 264,
    EXTERN = 265,
    AUTO = 266,
    INT = 267,
    VOID = 268,
    FUNCTION = 269,
    APPLY = 270,
    LEAF = 271,
    IF = 272,
    ELSE = 273,
    WHILE = 274,
    CONTINUE = 275,
    BREAK = 276,
    RETURN = 277
  };
#endif
/* Tokens.  */
#define IDENTIFIER 258
#define CONSTANT 259
#define STRING_LITERAL 260
#define LE_OP 261
#define GE_OP 262
#define EQ_OP 263
#define NE_OP 264
#define EXTERN 265
#define AUTO 266
#define INT 267
#define VOID 268
#define FUNCTION 269
#define APPLY 270
#define LEAF 271
#define IF 272
#define ELSE 273
#define WHILE 274
#define CONTINUE 275
#define BREAK 276
#define RETURN 277

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;

int yyparse (void);



/* Copy the second part of user declarations.  */

#line 166 "y.tab.c" /* yacc.c:358  */

#ifdef short
# undef short
#endif

#ifdef YYTYPE_UINT8
typedef YYTYPE_UINT8 yytype_uint8;
#else
typedef unsigned char yytype_uint8;
#endif

#ifdef YYTYPE_INT8
typedef YYTYPE_INT8 yytype_int8;
#else
typedef signed char yytype_int8;
#endif

#ifdef YYTYPE_UINT16
typedef YYTYPE_UINT16 yytype_uint16;
#else
typedef unsigned short int yytype_uint16;
#endif

#ifdef YYTYPE_INT16
typedef YYTYPE_INT16 yytype_int16;
#else
typedef short int yytype_int16;
#endif

#ifndef YYSIZE_T
# ifdef __SIZE_TYPE__
#  define YYSIZE_T __SIZE_TYPE__
# elif defined size_t
#  define YYSIZE_T size_t
# elif ! defined YYSIZE_T
#  include <stddef.h> /* INFRINGES ON USER NAME SPACE */
#  define YYSIZE_T size_t
# else
#  define YYSIZE_T unsigned int
# endif
#endif

#define YYSIZE_MAXIMUM ((YYSIZE_T) -1)

#ifndef YY_
# if defined YYENABLE_NLS && YYENABLE_NLS
#  if ENABLE_NLS
#   include <libintl.h> /* INFRINGES ON USER NAME SPACE */
#   define YY_(Msgid) dgettext ("bison-runtime", Msgid)
#  endif
# endif
# ifndef YY_
#  define YY_(Msgid) Msgid
# endif
#endif

#ifndef YY_ATTRIBUTE
# if (defined __GNUC__                                               \
      && (2 < __GNUC__ || (__GNUC__ == 2 && 96 <= __GNUC_MINOR__)))  \
     || defined __SUNPRO_C && 0x5110 <= __SUNPRO_C
#  define YY_ATTRIBUTE(Spec) __attribute__(Spec)
# else
#  define YY_ATTRIBUTE(Spec) /* empty */
# endif
#endif

#ifndef YY_ATTRIBUTE_PURE
# define YY_ATTRIBUTE_PURE   YY_ATTRIBUTE ((__pure__))
#endif

#ifndef YY_ATTRIBUTE_UNUSED
# define YY_ATTRIBUTE_UNUSED YY_ATTRIBUTE ((__unused__))
#endif

#if !defined _Noreturn \
     && (!defined __STDC_VERSION__ || __STDC_VERSION__ < 201112)
# if defined _MSC_VER && 1200 <= _MSC_VER
#  define _Noreturn __declspec (noreturn)
# else
#  define _Noreturn YY_ATTRIBUTE ((__noreturn__))
# endif
#endif

/* Suppress unused-variable warnings by "using" E.  */
#if ! defined lint || defined __GNUC__
# define YYUSE(E) ((void) (E))
#else
# define YYUSE(E) /* empty */
#endif

#if defined __GNUC__ && 407 <= __GNUC__ * 100 + __GNUC_MINOR__
/* Suppress an incorrect diagnostic about yylval being uninitialized.  */
# define YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN \
    _Pragma ("GCC diagnostic push") \
    _Pragma ("GCC diagnostic ignored \"-Wuninitialized\"")\
    _Pragma ("GCC diagnostic ignored \"-Wmaybe-uninitialized\"")
# define YY_IGNORE_MAYBE_UNINITIALIZED_END \
    _Pragma ("GCC diagnostic pop")
#else
# define YY_INITIAL_VALUE(Value) Value
#endif
#ifndef YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
# define YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
# define YY_IGNORE_MAYBE_UNINITIALIZED_END
#endif
#ifndef YY_INITIAL_VALUE
# define YY_INITIAL_VALUE(Value) /* Nothing. */
#endif


#if ! defined yyoverflow || YYERROR_VERBOSE

/* The parser invokes alloca or malloc; define the necessary symbols.  */

# ifdef YYSTACK_USE_ALLOCA
#  if YYSTACK_USE_ALLOCA
#   ifdef __GNUC__
#    define YYSTACK_ALLOC __builtin_alloca
#   elif defined __BUILTIN_VA_ARG_INCR
#    include <alloca.h> /* INFRINGES ON USER NAME SPACE */
#   elif defined _AIX
#    define YYSTACK_ALLOC __alloca
#   elif defined _MSC_VER
#    include <malloc.h> /* INFRINGES ON USER NAME SPACE */
#    define alloca _alloca
#   else
#    define YYSTACK_ALLOC alloca
#    if ! defined _ALLOCA_H && ! defined EXIT_SUCCESS
#     include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
      /* Use EXIT_SUCCESS as a witness for stdlib.h.  */
#     ifndef EXIT_SUCCESS
#      define EXIT_SUCCESS 0
#     endif
#    endif
#   endif
#  endif
# endif

# ifdef YYSTACK_ALLOC
   /* Pacify GCC's 'empty if-body' warning.  */
#  define YYSTACK_FREE(Ptr) do { /* empty */; } while (0)
#  ifndef YYSTACK_ALLOC_MAXIMUM
    /* The OS might guarantee only one guard page at the bottom of the stack,
       and a page size can be as small as 4096 bytes.  So we cannot safely
       invoke alloca (N) if N exceeds 4096.  Use a slightly smaller number
       to allow for a few compiler-allocated temporary stack slots.  */
#   define YYSTACK_ALLOC_MAXIMUM 4032 /* reasonable circa 2006 */
#  endif
# else
#  define YYSTACK_ALLOC YYMALLOC
#  define YYSTACK_FREE YYFREE
#  ifndef YYSTACK_ALLOC_MAXIMUM
#   define YYSTACK_ALLOC_MAXIMUM YYSIZE_MAXIMUM
#  endif
#  if (defined __cplusplus && ! defined EXIT_SUCCESS \
       && ! ((defined YYMALLOC || defined malloc) \
             && (defined YYFREE || defined free)))
#   include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#   ifndef EXIT_SUCCESS
#    define EXIT_SUCCESS 0
#   endif
#  endif
#  ifndef YYMALLOC
#   define YYMALLOC malloc
#   if ! defined malloc && ! defined EXIT_SUCCESS
void *malloc (YYSIZE_T); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
#  ifndef YYFREE
#   define YYFREE free
#   if ! defined free && ! defined EXIT_SUCCESS
void free (void *); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
# endif
#endif /* ! defined yyoverflow || YYERROR_VERBOSE */


#if (! defined yyoverflow \
     && (! defined __cplusplus \
         || (defined YYSTYPE_IS_TRIVIAL && YYSTYPE_IS_TRIVIAL)))

/* A type that is properly aligned for any stack member.  */
union yyalloc
{
  yytype_int16 yyss_alloc;
  YYSTYPE yyvs_alloc;
};

/* The size of the maximum gap between one aligned stack and the next.  */
# define YYSTACK_GAP_MAXIMUM (sizeof (union yyalloc) - 1)

/* The size of an array large to enough to hold all stacks, each with
   N elements.  */
# define YYSTACK_BYTES(N) \
     ((N) * (sizeof (yytype_int16) + sizeof (YYSTYPE)) \
      + YYSTACK_GAP_MAXIMUM)

# define YYCOPY_NEEDED 1

/* Relocate STACK from its old location to the new one.  The
   local variables YYSIZE and YYSTACKSIZE give the old and new number of
   elements in the stack, and YYPTR gives the new location of the
   stack.  Advance YYPTR to a properly aligned location for the next
   stack.  */
# define YYSTACK_RELOCATE(Stack_alloc, Stack)                           \
    do                                                                  \
      {                                                                 \
        YYSIZE_T yynewbytes;                                            \
        YYCOPY (&yyptr->Stack_alloc, Stack, yysize);                    \
        Stack = &yyptr->Stack_alloc;                                    \
        yynewbytes = yystacksize * sizeof (*Stack) + YYSTACK_GAP_MAXIMUM; \
        yyptr += yynewbytes / sizeof (*yyptr);                          \
      }                                                                 \
    while (0)

#endif

#if defined YYCOPY_NEEDED && YYCOPY_NEEDED
/* Copy COUNT objects from SRC to DST.  The source and destination do
   not overlap.  */
# ifndef YYCOPY
#  if defined __GNUC__ && 1 < __GNUC__
#   define YYCOPY(Dst, Src, Count) \
      __builtin_memcpy (Dst, Src, (Count) * sizeof (*(Src)))
#  else
#   define YYCOPY(Dst, Src, Count)              \
      do                                        \
        {                                       \
          YYSIZE_T yyi;                         \
          for (yyi = 0; yyi < (Count); yyi++)   \
            (Dst)[yyi] = (Src)[yyi];            \
        }                                       \
      while (0)
#  endif
# endif
#endif /* !YYCOPY_NEEDED */

/* YYFINAL -- State number of the termination state.  */
#define YYFINAL  22
/* YYLAST -- Last index in YYTABLE.  */
#define YYLAST   380

/* YYNTOKENS -- Number of terminals.  */
#define YYNTOKENS  39
/* YYNNTS -- Number of nonterminals.  */
#define YYNNTS  38
/* YYNRULES -- Number of rules.  */
#define YYNRULES  107
/* YYNSTATES -- Number of states.  */
#define YYNSTATES  171

/* YYTRANSLATE[YYX] -- Symbol number corresponding to YYX as returned
   by yylex, with out-of-bounds checking.  */
#define YYUNDEFTOK  2
#define YYMAXUTOK   277

#define YYTRANSLATE(YYX)                                                \
  ((unsigned int) (YYX) <= YYMAXUTOK ? yytranslate[YYX] : YYUNDEFTOK)

/* YYTRANSLATE[TOKEN-NUM] -- Symbol number corresponding to TOKEN-NUM
   as returned by yylex, without out-of-bounds checking.  */
static const yytype_uint8 yytranslate[] =
{
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,    30,     2,     2,     2,    32,    26,     2,
      23,    24,    27,    28,    25,    29,     2,    31,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,    36,
      33,    35,    34,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,    37,     2,    38,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22
};

#if YYDEBUG
  /* YYRLINE[YYN] -- Source line where rule number YYN was defined.  */
static const yytype_uint16 yyrline[] =
{
       0,    21,    21,    25,    26,    27,    28,    32,    33,    34,
      39,    40,    45,    46,    50,    51,    52,    53,    54,    58,
      59,    61,    63,    68,    69,    71,    76,    77,    79,    81,
      83,    88,    89,    91,    96,    97,   102,   103,   107,   108,
     109,   114,   115,   117,   118,   122,   123,   127,   128,   132,
     133,   137,   138,   139,   143,   144,   148,   149,   150,   151,
     152,   156,   157,   161,   162,   166,   167,   168,   172,   173,
     179,   180,   181,   185,   186,   187,   188,   189,   193,   194,
     195,   196,   197,   201,   202,   203,   204,   208,   209,   213,
     214,   218,   219,   223,   224,   230,   234,   235,   236,   237,
     241,   242,   246,   247,   251,   253,   255,   257
};
#endif

#if YYDEBUG || YYERROR_VERBOSE || 0
/* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
   First, the terminals, then, starting at YYNTOKENS, nonterminals.  */
static const char *const yytname[] =
{
  "$end", "error", "$undefined", "IDENTIFIER", "CONSTANT",
  "STRING_LITERAL", "LE_OP", "GE_OP", "EQ_OP", "NE_OP", "EXTERN", "AUTO",
  "INT", "VOID", "FUNCTION", "APPLY", "LEAF", "IF", "ELSE", "WHILE",
  "CONTINUE", "BREAK", "RETURN", "'('", "')'", "','", "'&'", "'*'", "'+'",
  "'-'", "'!'", "'/'", "'%'", "'<'", "'>'", "'='", "';'", "'{'", "'}'",
  "$accept", "goal", "primary_expression", "postfix_expression",
  "argument_expression_list", "unary_expression", "unary_operator",
  "multiplicative_expression", "additive_expression",
  "relational_expression", "equality_expression", "assignment_expression",
  "expression", "declaration", "declaration_specifiers",
  "init_declarator_list", "init_declarator", "storage_class_specifier",
  "type_specifier", "declarator", "direct_declarator", "pointer",
  "parameter_list", "parameter_declaration", "identifier_list",
  "abstract_declarator", "direct_abstract_declarator", "statement",
  "compound_statement", "declaration_list", "statement_list",
  "expression_statement", "selection_statement", "iteration_statement",
  "jump_statement", "translation_unit", "external_declaration",
  "function_definition", YY_NULLPTR
};
#endif

# ifdef YYPRINT
/* YYTOKNUM[NUM] -- (External) token number corresponding to the
   (internal) symbol number NUM (which must be that of a token).  */
static const yytype_uint16 yytoknum[] =
{
       0,   256,   257,   258,   259,   260,   261,   262,   263,   264,
     265,   266,   267,   268,   269,   270,   271,   272,   273,   274,
     275,   276,   277,    40,    41,    44,    38,    42,    43,    45,
      33,    47,    37,    60,    62,    61,    59,   123,   125
};
# endif

#define YYPACT_NINF -112

#define yypact_value_is_default(Yystate) \
  (!!((Yystate) == (-112)))

#define YYTABLE_NINF -57

#define yytable_value_is_error(Yytable_value) \
  0

  /* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
     STATE-NUM.  */
static const yytype_int16 yypact[] =
{
     351,  -112,  -112,  -112,  -112,  -112,  -112,    22,    -8,    24,
    -112,    10,   210,   210,    17,    19,    13,   351,  -112,  -112,
       8,  -112,  -112,  -112,   -15,  -112,    71,  -112,  -112,   124,
    -112,  -112,    17,  -112,   356,    19,  -112,  -112,    22,  -112,
     301,  -112,    17,   258,  -112,  -112,    30,    35,    57,    64,
     273,   312,  -112,    -8,  -112,  -112,  -112,  -112,  -112,  -112,
      37,    44,   301,    93,   -11,     5,   109,  -112,    61,  -112,
    -112,   153,   181,  -112,  -112,  -112,  -112,  -112,  -112,  -112,
    -112,    32,   115,  -112,   144,  -112,    67,  -112,   301,  -112,
    -112,  -112,   301,   301,  -112,  -112,  -112,    63,   163,   284,
     301,  -112,   301,   301,   301,   301,   301,   301,   301,   301,
     301,   301,   301,   301,  -112,  -112,   209,  -112,  -112,   333,
    -112,    73,  -112,    82,  -112,   210,  -112,   107,   191,   219,
    -112,  -112,  -112,   224,  -112,  -112,  -112,  -112,  -112,  -112,
      93,    93,   -11,   -11,   -11,   -11,     5,     5,  -112,  -112,
    -112,   226,    88,    82,   182,  -112,  -112,   237,   237,  -112,
     301,  -112,  -112,  -112,   228,    96,  -112,  -112,  -112,   237,
    -112
};

  /* YYDEFACT[STATE-NUM] -- Default reduction number in state STATE-NUM.
     Performed when YYTABLE does not specify something else to do.  Zero
     means the default is an error.  */
static const yytype_uint8 yydefact[] =
{
       0,    56,    49,    50,    52,    51,    53,     0,    61,     0,
     103,     0,    41,    43,     0,    55,     0,     2,   100,    39,
       0,    62,     1,    38,     0,    45,    47,    42,    44,     0,
      87,   107,     0,    39,     0,    54,   101,    57,     0,    40,
       0,   105,     0,     3,     4,     5,     0,     0,     0,     0,
       0,     0,    14,    15,    16,    17,    18,    91,    83,     7,
      12,    19,     0,    23,    26,    31,    34,    36,     0,    89,
      78,     0,     0,    79,    80,    81,    82,    88,   106,    68,
      60,    67,     0,    63,     0,    46,    47,     3,     0,    15,
      48,   104,     0,     0,    96,    97,    98,     0,     0,     0,
       0,    13,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,    92,    85,     0,    84,    90,     0,
      65,    70,    66,    71,    58,     0,    59,     0,     0,     0,
      99,     6,     8,     0,    10,    35,    20,    21,    22,    19,
      24,    25,    29,    30,    27,    28,    32,    33,    37,    86,
      74,     0,     0,    72,     0,    64,    69,     0,     0,     9,
       0,    75,    73,    76,     0,    93,    95,    11,    77,     0,
      94
};

  /* YYPGOTO[NTERM-NUM].  */
static const yytype_int16 yypgoto[] =
{
    -112,  -112,  -112,  -112,  -112,   -39,  -112,    43,    23,   122,
    -112,   -38,   -36,     9,   -12,  -112,    83,  -112,  -112,    -4,
     -10,    -3,  -111,    -2,  -112,     7,    34,   -68,    77,    66,
     100,  -112,  -112,  -112,  -112,  -112,   142,    87
};

  /* YYDEFGOTO[NTERM-NUM].  */
static const yytype_int16 yydefgoto[] =
{
      -1,     9,    59,    60,   133,    61,    62,    63,    64,    65,
      66,    67,    68,    30,    11,    24,    25,    12,    13,    14,
      15,    16,    82,    83,    84,   122,   123,    69,    70,    32,
      72,    73,    74,    75,    76,    17,    18,    33
};

  /* YYTABLE[YYPACT[STATE-NUM]] -- What to do in state STATE-NUM.  If
     positive, shift that token.  If negative, reduce the rule whose
     number is the opposite.  If YYTABLE_NINF, syntax error.  */
static const yytype_int16 yytable[] =
{
      27,    28,    90,    20,   118,    21,    35,    26,   151,    10,
      38,   107,   108,     1,    97,    98,     1,   105,   106,     8,
       1,    39,    81,   101,    22,     1,    10,     2,     3,     4,
       5,     6,    37,     7,    86,     1,     7,     8,   109,   110,
       7,    77,    34,   164,     8,     7,    23,    20,   118,     8,
      21,    77,    98,    92,    29,   119,   128,   129,    93,     8,
      99,   134,   135,   136,   137,   138,   139,   139,   139,   139,
     139,   139,   139,   139,     1,   148,     1,   120,   121,   100,
      77,     2,     3,     4,     5,     6,   113,    19,   113,   165,
     166,    31,    42,    94,     7,    71,   119,   114,     8,   130,
      95,   170,    40,    41,    19,   154,    40,    81,    29,    78,
     156,    35,   162,    81,   169,    20,   121,   111,   112,    91,
     102,    85,   167,   155,   103,   104,   152,    43,    44,    45,
     142,   143,   144,   145,     2,     3,     4,     5,     6,   124,
     125,    46,    81,    47,    48,    49,    50,    51,   140,   141,
      52,    53,    54,    55,    56,   153,    43,    44,    45,    36,
      57,    29,    58,     2,     3,     4,     5,     6,   126,   127,
      46,   116,    47,    48,    49,    50,    51,     0,     0,    52,
      53,    54,    55,    56,    87,    44,    45,   131,   113,    57,
      29,   115,     2,     3,     4,     5,     6,     0,    46,     0,
      47,    48,    49,    50,    88,     0,   163,    52,    89,    54,
      55,    56,    87,    44,    45,   157,   113,    57,    29,   117,
       2,     3,     4,     5,     6,     0,    46,     0,    47,    48,
      49,    50,    88,   146,   147,    52,    89,    54,    55,    56,
      87,    44,    45,   158,   113,    57,    29,   149,   159,   160,
     161,   125,   168,   125,    46,     0,    47,    48,    49,    50,
      88,   -56,     0,    52,    89,    54,    55,    56,   -56,   -56,
     -56,   -56,   -56,    57,    29,     0,    87,    44,    45,     0,
       0,     0,     0,     0,     0,     0,     0,    87,    44,    45,
       0,     0,     0,     0,     0,   -56,    88,     0,     0,    52,
      89,    54,    55,    56,    87,    44,    45,    88,   132,    96,
      52,    89,    54,    55,    56,    43,    44,    45,     0,     0,
       0,     0,     0,     0,    88,     0,     0,    52,    89,    54,
      55,    56,     0,     0,     0,    51,     1,     0,    52,    53,
      54,    55,    56,     2,     3,     4,     5,     6,     0,     0,
       0,     0,     0,     0,     1,     0,   119,   150,     0,    79,
       8,     2,     3,     4,     5,     6,     2,     3,     4,     5,
       6,     0,     0,     0,     7,     0,     0,     0,     8,     0,
      80
};

static const yytype_int16 yycheck[] =
{
      12,    13,    40,     7,    72,     8,    16,    11,   119,     0,
      25,     6,     7,     3,    50,    51,     3,    28,    29,    27,
       3,    36,    34,    62,     0,     3,    17,    10,    11,    12,
      13,    14,    24,    23,    38,     3,    23,    27,    33,    34,
      23,    32,    23,   154,    27,    23,    36,    51,   116,    27,
      53,    42,    88,    23,    37,    23,    92,    93,    23,    27,
      23,    99,   100,   102,   103,   104,   105,   106,   107,   108,
     109,   110,   111,   112,     3,   113,     3,    81,    81,    35,
      71,    10,    11,    12,    13,    14,    25,     0,    25,   157,
     158,    14,    26,    36,    23,    29,    23,    36,    27,    36,
      36,   169,    35,    26,    17,    23,    35,   119,    37,    32,
       3,   121,    24,   125,    18,   119,   119,     8,     9,    42,
      27,    38,   160,   125,    31,    32,   119,     3,     4,     5,
     107,   108,   109,   110,    10,    11,    12,    13,    14,    24,
      25,    17,   154,    19,    20,    21,    22,    23,   105,   106,
      26,    27,    28,    29,    30,   121,     3,     4,     5,    17,
      36,    37,    38,    10,    11,    12,    13,    14,    24,    25,
      17,    71,    19,    20,    21,    22,    23,    -1,    -1,    26,
      27,    28,    29,    30,     3,     4,     5,    24,    25,    36,
      37,    38,    10,    11,    12,    13,    14,    -1,    17,    -1,
      19,    20,    21,    22,    23,    -1,    24,    26,    27,    28,
      29,    30,     3,     4,     5,    24,    25,    36,    37,    38,
      10,    11,    12,    13,    14,    -1,    17,    -1,    19,    20,
      21,    22,    23,   111,   112,    26,    27,    28,    29,    30,
       3,     4,     5,    24,    25,    36,    37,    38,    24,    25,
      24,    25,    24,    25,    17,    -1,    19,    20,    21,    22,
      23,     3,    -1,    26,    27,    28,    29,    30,    10,    11,
      12,    13,    14,    36,    37,    -1,     3,     4,     5,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,     3,     4,     5,
      -1,    -1,    -1,    -1,    -1,    37,    23,    -1,    -1,    26,
      27,    28,    29,    30,     3,     4,     5,    23,    24,    36,
      26,    27,    28,    29,    30,     3,     4,     5,    -1,    -1,
      -1,    -1,    -1,    -1,    23,    -1,    -1,    26,    27,    28,
      29,    30,    -1,    -1,    -1,    23,     3,    -1,    26,    27,
      28,    29,    30,    10,    11,    12,    13,    14,    -1,    -1,
      -1,    -1,    -1,    -1,     3,    -1,    23,    24,    -1,     3,
      27,    10,    11,    12,    13,    14,    10,    11,    12,    13,
      14,    -1,    -1,    -1,    23,    -1,    -1,    -1,    27,    -1,
      24
};

  /* YYSTOS[STATE-NUM] -- The (internal number of the) accessing
     symbol of state STATE-NUM.  */
static const yytype_uint8 yystos[] =
{
       0,     3,    10,    11,    12,    13,    14,    23,    27,    40,
      52,    53,    56,    57,    58,    59,    60,    74,    75,    76,
      58,    60,     0,    36,    54,    55,    58,    53,    53,    37,
      52,    67,    68,    76,    23,    59,    75,    24,    25,    36,
      35,    67,    68,     3,     4,     5,    17,    19,    20,    21,
      22,    23,    26,    27,    28,    29,    30,    36,    38,    41,
      42,    44,    45,    46,    47,    48,    49,    50,    51,    66,
      67,    68,    69,    70,    71,    72,    73,    52,    67,     3,
      24,    53,    61,    62,    63,    55,    58,     3,    23,    27,
      50,    67,    23,    23,    36,    36,    36,    51,    51,    23,
      35,    44,    27,    31,    32,    28,    29,     6,     7,    33,
      34,     8,     9,    25,    36,    38,    69,    38,    66,    23,
      58,    60,    64,    65,    24,    25,    24,    25,    51,    51,
      36,    24,    24,    43,    50,    50,    44,    44,    44,    44,
      46,    46,    47,    47,    47,    47,    48,    48,    50,    38,
      24,    61,    64,    65,    23,    62,     3,    24,    24,    24,
      25,    24,    24,    24,    61,    66,    66,    50,    24,    18,
      66
};

  /* YYR1[YYN] -- Symbol number of symbol that rule YYN derives.  */
static const yytype_uint8 yyr1[] =
{
       0,    39,    40,    41,    41,    41,    41,    42,    42,    42,
      43,    43,    44,    44,    45,    45,    45,    45,    45,    46,
      46,    46,    46,    47,    47,    47,    48,    48,    48,    48,
      48,    49,    49,    49,    50,    50,    51,    51,    52,    52,
      52,    53,    53,    53,    53,    54,    54,    55,    55,    56,
      56,    57,    57,    57,    58,    58,    59,    59,    59,    59,
      59,    60,    60,    61,    61,    62,    62,    62,    63,    63,
      64,    64,    64,    65,    65,    65,    65,    65,    66,    66,
      66,    66,    66,    67,    67,    67,    67,    68,    68,    69,
      69,    70,    70,    71,    71,    72,    73,    73,    73,    73,
      74,    74,    75,    75,    76,    76,    76,    76
};

  /* YYR2[YYN] -- Number of symbols on the right hand side of rule YYN.  */
static const yytype_uint8 yyr2[] =
{
       0,     2,     1,     1,     1,     1,     3,     1,     3,     4,
       1,     3,     1,     2,     1,     1,     1,     1,     1,     1,
       3,     3,     3,     1,     3,     3,     1,     3,     3,     3,
       3,     1,     3,     3,     1,     3,     1,     3,     2,     1,
       3,     1,     2,     1,     2,     1,     3,     1,     3,     1,
       1,     1,     1,     1,     2,     1,     1,     3,     4,     4,
       3,     1,     2,     1,     3,     2,     2,     1,     1,     3,
       1,     1,     2,     3,     2,     3,     3,     4,     1,     1,
       1,     1,     1,     2,     3,     3,     4,     1,     2,     1,
       2,     1,     2,     5,     7,     5,     2,     2,     2,     3,
       1,     2,     1,     1,     4,     3,     3,     2
};


#define yyerrok         (yyerrstatus = 0)
#define yyclearin       (yychar = YYEMPTY)
#define YYEMPTY         (-2)
#define YYEOF           0

#define YYACCEPT        goto yyacceptlab
#define YYABORT         goto yyabortlab
#define YYERROR         goto yyerrorlab


#define YYRECOVERING()  (!!yyerrstatus)

#define YYBACKUP(Token, Value)                                  \
do                                                              \
  if (yychar == YYEMPTY)                                        \
    {                                                           \
      yychar = (Token);                                         \
      yylval = (Value);                                         \
      YYPOPSTACK (yylen);                                       \
      yystate = *yyssp;                                         \
      goto yybackup;                                            \
    }                                                           \
  else                                                          \
    {                                                           \
      yyerror (YY_("syntax error: cannot back up")); \
      YYERROR;                                                  \
    }                                                           \
while (0)

/* Error token number */
#define YYTERROR        1
#define YYERRCODE       256



/* Enable debugging if requested.  */
#if YYDEBUG

# ifndef YYFPRINTF
#  include <stdio.h> /* INFRINGES ON USER NAME SPACE */
#  define YYFPRINTF fprintf
# endif

# define YYDPRINTF(Args)                        \
do {                                            \
  if (yydebug)                                  \
    YYFPRINTF Args;                             \
} while (0)

/* This macro is provided for backward compatibility. */
#ifndef YY_LOCATION_PRINT
# define YY_LOCATION_PRINT(File, Loc) ((void) 0)
#endif


# define YY_SYMBOL_PRINT(Title, Type, Value, Location)                    \
do {                                                                      \
  if (yydebug)                                                            \
    {                                                                     \
      YYFPRINTF (stderr, "%s ", Title);                                   \
      yy_symbol_print (stderr,                                            \
                  Type, Value); \
      YYFPRINTF (stderr, "\n");                                           \
    }                                                                     \
} while (0)


/*----------------------------------------.
| Print this symbol's value on YYOUTPUT.  |
`----------------------------------------*/

static void
yy_symbol_value_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
{
  FILE *yyo = yyoutput;
  YYUSE (yyo);
  if (!yyvaluep)
    return;
# ifdef YYPRINT
  if (yytype < YYNTOKENS)
    YYPRINT (yyoutput, yytoknum[yytype], *yyvaluep);
# endif
  YYUSE (yytype);
}


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

static void
yy_symbol_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
{
  YYFPRINTF (yyoutput, "%s %s (",
             yytype < YYNTOKENS ? "token" : "nterm", yytname[yytype]);

  yy_symbol_value_print (yyoutput, yytype, yyvaluep);
  YYFPRINTF (yyoutput, ")");
}

/*------------------------------------------------------------------.
| yy_stack_print -- Print the state stack from its BOTTOM up to its |
| TOP (included).                                                   |
`------------------------------------------------------------------*/

static void
yy_stack_print (yytype_int16 *yybottom, yytype_int16 *yytop)
{
  YYFPRINTF (stderr, "Stack now");
  for (; yybottom <= yytop; yybottom++)
    {
      int yybot = *yybottom;
      YYFPRINTF (stderr, " %d", yybot);
    }
  YYFPRINTF (stderr, "\n");
}

# define YY_STACK_PRINT(Bottom, Top)                            \
do {                                                            \
  if (yydebug)                                                  \
    yy_stack_print ((Bottom), (Top));                           \
} while (0)


/*------------------------------------------------.
| Report that the YYRULE is going to be reduced.  |
`------------------------------------------------*/

static void
yy_reduce_print (yytype_int16 *yyssp, YYSTYPE *yyvsp, int yyrule)
{
  unsigned long int yylno = yyrline[yyrule];
  int yynrhs = yyr2[yyrule];
  int yyi;
  YYFPRINTF (stderr, "Reducing stack by rule %d (line %lu):\n",
             yyrule - 1, yylno);
  /* The symbols being reduced.  */
  for (yyi = 0; yyi < yynrhs; yyi++)
    {
      YYFPRINTF (stderr, "   $%d = ", yyi + 1);
      yy_symbol_print (stderr,
                       yystos[yyssp[yyi + 1 - yynrhs]],
                       &(yyvsp[(yyi + 1) - (yynrhs)])
                                              );
      YYFPRINTF (stderr, "\n");
    }
}

# define YY_REDUCE_PRINT(Rule)          \
do {                                    \
  if (yydebug)                          \
    yy_reduce_print (yyssp, yyvsp, Rule); \
} while (0)

/* Nonzero means print parse trace.  It is left uninitialized so that
   multiple parsers can coexist.  */
int yydebug;
#else /* !YYDEBUG */
# define YYDPRINTF(Args)
# define YY_SYMBOL_PRINT(Title, Type, Value, Location)
# define YY_STACK_PRINT(Bottom, Top)
# define YY_REDUCE_PRINT(Rule)
#endif /* !YYDEBUG */


/* YYINITDEPTH -- initial size of the parser's stacks.  */
#ifndef YYINITDEPTH
# define YYINITDEPTH 200
#endif

/* YYMAXDEPTH -- maximum size the stacks can grow to (effective only
   if the built-in stack extension method is used).

   Do not make this value too large; the results are undefined if
   YYSTACK_ALLOC_MAXIMUM < YYSTACK_BYTES (YYMAXDEPTH)
   evaluated with infinite-precision integer arithmetic.  */

#ifndef YYMAXDEPTH
# define YYMAXDEPTH 10000
#endif


#if YYERROR_VERBOSE

# ifndef yystrlen
#  if defined __GLIBC__ && defined _STRING_H
#   define yystrlen strlen
#  else
/* Return the length of YYSTR.  */
static YYSIZE_T
yystrlen (const char *yystr)
{
  YYSIZE_T yylen;
  for (yylen = 0; yystr[yylen]; yylen++)
    continue;
  return yylen;
}
#  endif
# endif

# ifndef yystpcpy
#  if defined __GLIBC__ && defined _STRING_H && defined _GNU_SOURCE
#   define yystpcpy stpcpy
#  else
/* Copy YYSRC to YYDEST, returning the address of the terminating '\0' in
   YYDEST.  */
static char *
yystpcpy (char *yydest, const char *yysrc)
{
  char *yyd = yydest;
  const char *yys = yysrc;

  while ((*yyd++ = *yys++) != '\0')
    continue;

  return yyd - 1;
}
#  endif
# endif

# ifndef yytnamerr
/* Copy to YYRES the contents of YYSTR after stripping away unnecessary
   quotes and backslashes, so that it's suitable for yyerror.  The
   heuristic is that double-quoting is unnecessary unless the string
   contains an apostrophe, a comma, or backslash (other than
   backslash-backslash).  YYSTR is taken from yytname.  If YYRES is
   null, do not copy; instead, return the length of what the result
   would have been.  */
static YYSIZE_T
yytnamerr (char *yyres, const char *yystr)
{
  if (*yystr == '"')
    {
      YYSIZE_T yyn = 0;
      char const *yyp = yystr;

      for (;;)
        switch (*++yyp)
          {
          case '\'':
          case ',':
            goto do_not_strip_quotes;

          case '\\':
            if (*++yyp != '\\')
              goto do_not_strip_quotes;
            /* Fall through.  */
          default:
            if (yyres)
              yyres[yyn] = *yyp;
            yyn++;
            break;

          case '"':
            if (yyres)
              yyres[yyn] = '\0';
            return yyn;
          }
    do_not_strip_quotes: ;
    }

  if (! yyres)
    return yystrlen (yystr);

  return yystpcpy (yyres, yystr) - yyres;
}
# endif

/* Copy into *YYMSG, which is of size *YYMSG_ALLOC, an error message
   about the unexpected token YYTOKEN for the state stack whose top is
   YYSSP.

   Return 0 if *YYMSG was successfully written.  Return 1 if *YYMSG is
   not large enough to hold the message.  In that case, also set
   *YYMSG_ALLOC to the required number of bytes.  Return 2 if the
   required number of bytes is too large to store.  */
static int
yysyntax_error (YYSIZE_T *yymsg_alloc, char **yymsg,
                yytype_int16 *yyssp, int yytoken)
{
  YYSIZE_T yysize0 = yytnamerr (YY_NULLPTR, yytname[yytoken]);
  YYSIZE_T yysize = yysize0;
  enum { YYERROR_VERBOSE_ARGS_MAXIMUM = 5 };
  /* Internationalized format string. */
  const char *yyformat = YY_NULLPTR;
  /* Arguments of yyformat. */
  char const *yyarg[YYERROR_VERBOSE_ARGS_MAXIMUM];
  /* Number of reported tokens (one for the "unexpected", one per
     "expected"). */
  int yycount = 0;

  /* There are many possibilities here to consider:
     - If this state is a consistent state with a default action, then
       the only way this function was invoked is if the default action
       is an error action.  In that case, don't check for expected
       tokens because there are none.
     - The only way there can be no lookahead present (in yychar) is if
       this state is a consistent state with a default action.  Thus,
       detecting the absence of a lookahead is sufficient to determine
       that there is no unexpected or expected token to report.  In that
       case, just report a simple "syntax error".
     - Don't assume there isn't a lookahead just because this state is a
       consistent state with a default action.  There might have been a
       previous inconsistent state, consistent state with a non-default
       action, or user semantic action that manipulated yychar.
     - Of course, the expected token list depends on states to have
       correct lookahead information, and it depends on the parser not
       to perform extra reductions after fetching a lookahead from the
       scanner and before detecting a syntax error.  Thus, state merging
       (from LALR or IELR) and default reductions corrupt the expected
       token list.  However, the list is correct for canonical LR with
       one exception: it will still contain any token that will not be
       accepted due to an error action in a later state.
  */
  if (yytoken != YYEMPTY)
    {
      int yyn = yypact[*yyssp];
      yyarg[yycount++] = yytname[yytoken];
      if (!yypact_value_is_default (yyn))
        {
          /* Start YYX at -YYN if negative to avoid negative indexes in
             YYCHECK.  In other words, skip the first -YYN actions for
             this state because they are default actions.  */
          int yyxbegin = yyn < 0 ? -yyn : 0;
          /* Stay within bounds of both yycheck and yytname.  */
          int yychecklim = YYLAST - yyn + 1;
          int yyxend = yychecklim < YYNTOKENS ? yychecklim : YYNTOKENS;
          int yyx;

          for (yyx = yyxbegin; yyx < yyxend; ++yyx)
            if (yycheck[yyx + yyn] == yyx && yyx != YYTERROR
                && !yytable_value_is_error (yytable[yyx + yyn]))
              {
                if (yycount == YYERROR_VERBOSE_ARGS_MAXIMUM)
                  {
                    yycount = 1;
                    yysize = yysize0;
                    break;
                  }
                yyarg[yycount++] = yytname[yyx];
                {
                  YYSIZE_T yysize1 = yysize + yytnamerr (YY_NULLPTR, yytname[yyx]);
                  if (! (yysize <= yysize1
                         && yysize1 <= YYSTACK_ALLOC_MAXIMUM))
                    return 2;
                  yysize = yysize1;
                }
              }
        }
    }

  switch (yycount)
    {
# define YYCASE_(N, S)                      \
      case N:                               \
        yyformat = S;                       \
      break
      YYCASE_(0, YY_("syntax error"));
      YYCASE_(1, YY_("syntax error, unexpected %s"));
      YYCASE_(2, YY_("syntax error, unexpected %s, expecting %s"));
      YYCASE_(3, YY_("syntax error, unexpected %s, expecting %s or %s"));
      YYCASE_(4, YY_("syntax error, unexpected %s, expecting %s or %s or %s"));
      YYCASE_(5, YY_("syntax error, unexpected %s, expecting %s or %s or %s or %s"));
# undef YYCASE_
    }

  {
    YYSIZE_T yysize1 = yysize + yystrlen (yyformat);
    if (! (yysize <= yysize1 && yysize1 <= YYSTACK_ALLOC_MAXIMUM))
      return 2;
    yysize = yysize1;
  }

  if (*yymsg_alloc < yysize)
    {
      *yymsg_alloc = 2 * yysize;
      if (! (yysize <= *yymsg_alloc
             && *yymsg_alloc <= YYSTACK_ALLOC_MAXIMUM))
        *yymsg_alloc = YYSTACK_ALLOC_MAXIMUM;
      return 1;
    }

  /* Avoid sprintf, as that infringes on the user's name space.
     Don't have undefined behavior even if the translation
     produced a string with the wrong number of "%s"s.  */
  {
    char *yyp = *yymsg;
    int yyi = 0;
    while ((*yyp = *yyformat) != '\0')
      if (*yyp == '%' && yyformat[1] == 's' && yyi < yycount)
        {
          yyp += yytnamerr (yyp, yyarg[yyi++]);
          yyformat += 2;
        }
      else
        {
          yyp++;
          yyformat++;
        }
  }
  return 0;
}
#endif /* YYERROR_VERBOSE */

/*-----------------------------------------------.
| Release the memory associated to this symbol.  |
`-----------------------------------------------*/

static void
yydestruct (const char *yymsg, int yytype, YYSTYPE *yyvaluep)
{
  YYUSE (yyvaluep);
  if (!yymsg)
    yymsg = "Deleting";
  YY_SYMBOL_PRINT (yymsg, yytype, yyvaluep, yylocationp);

  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  YYUSE (yytype);
  YY_IGNORE_MAYBE_UNINITIALIZED_END
}




/* The lookahead symbol.  */
int yychar;

/* The semantic value of the lookahead symbol.  */
YYSTYPE yylval;
/* Number of syntax errors so far.  */
int yynerrs;


/*----------.
| yyparse.  |
`----------*/

int
yyparse (void)
{
    int yystate;
    /* Number of tokens to shift before error messages enabled.  */
    int yyerrstatus;

    /* The stacks and their tools:
       'yyss': related to states.
       'yyvs': related to semantic values.

       Refer to the stacks through separate pointers, to allow yyoverflow
       to reallocate them elsewhere.  */

    /* The state stack.  */
    yytype_int16 yyssa[YYINITDEPTH];
    yytype_int16 *yyss;
    yytype_int16 *yyssp;

    /* The semantic value stack.  */
    YYSTYPE yyvsa[YYINITDEPTH];
    YYSTYPE *yyvs;
    YYSTYPE *yyvsp;

    YYSIZE_T yystacksize;

  int yyn;
  int yyresult;
  /* Lookahead token as an internal (translated) token number.  */
  int yytoken = 0;
  /* The variables used to return semantic value and location from the
     action routines.  */
  YYSTYPE yyval;

#if YYERROR_VERBOSE
  /* Buffer for error messages, and its allocated size.  */
  char yymsgbuf[128];
  char *yymsg = yymsgbuf;
  YYSIZE_T yymsg_alloc = sizeof yymsgbuf;
#endif

#define YYPOPSTACK(N)   (yyvsp -= (N), yyssp -= (N))

  /* The number of symbols on the RHS of the reduced rule.
     Keep to zero when no symbol should be popped.  */
  int yylen = 0;

  yyssp = yyss = yyssa;
  yyvsp = yyvs = yyvsa;
  yystacksize = YYINITDEPTH;

  YYDPRINTF ((stderr, "Starting parse\n"));

  yystate = 0;
  yyerrstatus = 0;
  yynerrs = 0;
  yychar = YYEMPTY; /* Cause a token to be read.  */
  goto yysetstate;

/*------------------------------------------------------------.
| yynewstate -- Push a new state, which is found in yystate.  |
`------------------------------------------------------------*/
 yynewstate:
  /* In all cases, when you get here, the value and location stacks
     have just been pushed.  So pushing a state here evens the stacks.  */
  yyssp++;

 yysetstate:
  *yyssp = yystate;

  if (yyss + yystacksize - 1 <= yyssp)
    {
      /* Get the current used size of the three stacks, in elements.  */
      YYSIZE_T yysize = yyssp - yyss + 1;

#ifdef yyoverflow
      {
        /* Give user a chance to reallocate the stack.  Use copies of
           these so that the &'s don't force the real ones into
           memory.  */
        YYSTYPE *yyvs1 = yyvs;
        yytype_int16 *yyss1 = yyss;

        /* Each stack pointer address is followed by the size of the
           data in use in that stack, in bytes.  This used to be a
           conditional around just the two extra args, but that might
           be undefined if yyoverflow is a macro.  */
        yyoverflow (YY_("memory exhausted"),
                    &yyss1, yysize * sizeof (*yyssp),
                    &yyvs1, yysize * sizeof (*yyvsp),
                    &yystacksize);

        yyss = yyss1;
        yyvs = yyvs1;
      }
#else /* no yyoverflow */
# ifndef YYSTACK_RELOCATE
      goto yyexhaustedlab;
# else
      /* Extend the stack our own way.  */
      if (YYMAXDEPTH <= yystacksize)
        goto yyexhaustedlab;
      yystacksize *= 2;
      if (YYMAXDEPTH < yystacksize)
        yystacksize = YYMAXDEPTH;

      {
        yytype_int16 *yyss1 = yyss;
        union yyalloc *yyptr =
          (union yyalloc *) YYSTACK_ALLOC (YYSTACK_BYTES (yystacksize));
        if (! yyptr)
          goto yyexhaustedlab;
        YYSTACK_RELOCATE (yyss_alloc, yyss);
        YYSTACK_RELOCATE (yyvs_alloc, yyvs);
#  undef YYSTACK_RELOCATE
        if (yyss1 != yyssa)
          YYSTACK_FREE (yyss1);
      }
# endif
#endif /* no yyoverflow */

      yyssp = yyss + yysize - 1;
      yyvsp = yyvs + yysize - 1;

      YYDPRINTF ((stderr, "Stack size increased to %lu\n",
                  (unsigned long int) yystacksize));

      if (yyss + yystacksize - 1 <= yyssp)
        YYABORT;
    }

  YYDPRINTF ((stderr, "Entering state %d\n", yystate));

  if (yystate == YYFINAL)
    YYACCEPT;

  goto yybackup;

/*-----------.
| yybackup.  |
`-----------*/
yybackup:

  /* Do appropriate processing given the current state.  Read a
     lookahead token if we need one and don't already have one.  */

  /* First try to decide what to do without reference to lookahead token.  */
  yyn = yypact[yystate];
  if (yypact_value_is_default (yyn))
    goto yydefault;

  /* Not known => get a lookahead token if don't already have one.  */

  /* YYCHAR is either YYEMPTY or YYEOF or a valid lookahead symbol.  */
  if (yychar == YYEMPTY)
    {
      YYDPRINTF ((stderr, "Reading a token: "));
      yychar = yylex ();
    }

  if (yychar <= YYEOF)
    {
      yychar = yytoken = YYEOF;
      YYDPRINTF ((stderr, "Now at end of input.\n"));
    }
  else
    {
      yytoken = YYTRANSLATE (yychar);
      YY_SYMBOL_PRINT ("Next token is", yytoken, &yylval, &yylloc);
    }

  /* If the proper action on seeing token YYTOKEN is to reduce or to
     detect an error, take that action.  */
  yyn += yytoken;
  if (yyn < 0 || YYLAST < yyn || yycheck[yyn] != yytoken)
    goto yydefault;
  yyn = yytable[yyn];
  if (yyn <= 0)
    {
      if (yytable_value_is_error (yyn))
        goto yyerrlab;
      yyn = -yyn;
      goto yyreduce;
    }

  /* Count tokens shifted since error; after three, turn off error
     status.  */
  if (yyerrstatus)
    yyerrstatus--;

  /* Shift the lookahead token.  */
  YY_SYMBOL_PRINT ("Shifting", yytoken, &yylval, &yylloc);

  /* Discard the shifted token.  */
  yychar = YYEMPTY;

  yystate = yyn;
  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  *++yyvsp = yylval;
  YY_IGNORE_MAYBE_UNINITIALIZED_END

  goto yynewstate;


/*-----------------------------------------------------------.
| yydefault -- do the default action for the current state.  |
`-----------------------------------------------------------*/
yydefault:
  yyn = yydefact[yystate];
  if (yyn == 0)
    goto yyerrlab;
  goto yyreduce;


/*-----------------------------.
| yyreduce -- Do a reduction.  |
`-----------------------------*/
yyreduce:
  /* yyn is the number of a rule to reduce with.  */
  yylen = yyr2[yyn];

  /* If YYLEN is nonzero, implement the default value of the action:
     '$$ = $1'.

     Otherwise, the following line sets YYVAL to garbage.
     This behavior is undocumented and Bison
     users should not rely upon it.  Assigning to YYVAL
     unconditionally makes the parser a bit smaller, and it avoids a
     GCC warning that YYVAL may be used uninitialized.  */
  yyval = yyvsp[1-yylen];


  YY_REDUCE_PRINT (yyn);
  switch (yyn)
    {
        case 2:
#line 21 "C.y" /* yacc.c:1646  */
    { ans = (yyval) = (yyvsp[0]);}
#line 1415 "y.tab.c" /* yacc.c:1646  */
    break;

  case 3:
#line 25 "C.y" /* yacc.c:1646  */
    { (yyval) = make_leaf(lasttok); }
#line 1421 "y.tab.c" /* yacc.c:1646  */
    break;

  case 4:
#line 26 "C.y" /* yacc.c:1646  */
    { (yyval) = make_leaf(lasttok); }
#line 1427 "y.tab.c" /* yacc.c:1646  */
    break;

  case 5:
#line 27 "C.y" /* yacc.c:1646  */
    { (yyval) = make_leaf(lasttok); }
#line 1433 "y.tab.c" /* yacc.c:1646  */
    break;

  case 6:
#line 28 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[-1]); }
#line 1439 "y.tab.c" /* yacc.c:1646  */
    break;

  case 7:
#line 32 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1445 "y.tab.c" /* yacc.c:1646  */
    break;

  case 8:
#line 33 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(APPLY, (yyvsp[-2]), NULL); }
#line 1451 "y.tab.c" /* yacc.c:1646  */
    break;

  case 9:
#line 34 "C.y" /* yacc.c:1646  */
    {
				          (yyval) = make_node(APPLY, (yyvsp[-3]), (yyvsp[-1])); }
#line 1458 "y.tab.c" /* yacc.c:1646  */
    break;

  case 10:
#line 39 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1464 "y.tab.c" /* yacc.c:1646  */
    break;

  case 11:
#line 40 "C.y" /* yacc.c:1646  */
    {
          (yyval) = make_node(',', (yyvsp[-2]), (yyvsp[0])); }
#line 1471 "y.tab.c" /* yacc.c:1646  */
    break;

  case 12:
#line 45 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1477 "y.tab.c" /* yacc.c:1646  */
    break;

  case 13:
#line 46 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node((int)(yyvsp[-1]), (yyvsp[0]), NULL); }
#line 1483 "y.tab.c" /* yacc.c:1646  */
    break;

  case 14:
#line 50 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1489 "y.tab.c" /* yacc.c:1646  */
    break;

  case 15:
#line 51 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1495 "y.tab.c" /* yacc.c:1646  */
    break;

  case 16:
#line 52 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1501 "y.tab.c" /* yacc.c:1646  */
    break;

  case 17:
#line 53 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1507 "y.tab.c" /* yacc.c:1646  */
    break;

  case 18:
#line 54 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1513 "y.tab.c" /* yacc.c:1646  */
    break;

  case 19:
#line 58 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1519 "y.tab.c" /* yacc.c:1646  */
    break;

  case 20:
#line 59 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node('*', (yyvsp[-2]), (yyvsp[0])); }
#line 1526 "y.tab.c" /* yacc.c:1646  */
    break;

  case 21:
#line 61 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node('/', (yyvsp[-2]), (yyvsp[0])); }
#line 1533 "y.tab.c" /* yacc.c:1646  */
    break;

  case 22:
#line 63 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node('%', (yyvsp[-2]), (yyvsp[0])); }
#line 1540 "y.tab.c" /* yacc.c:1646  */
    break;

  case 23:
#line 68 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1546 "y.tab.c" /* yacc.c:1646  */
    break;

  case 24:
#line 69 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node('+', (yyvsp[-2]), (yyvsp[0])); }
#line 1553 "y.tab.c" /* yacc.c:1646  */
    break;

  case 25:
#line 71 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node('-', (yyvsp[-2]), (yyvsp[0])); }
#line 1560 "y.tab.c" /* yacc.c:1646  */
    break;

  case 26:
#line 76 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1566 "y.tab.c" /* yacc.c:1646  */
    break;

  case 27:
#line 77 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node('<', (yyvsp[-2]), (yyvsp[0])); }
#line 1573 "y.tab.c" /* yacc.c:1646  */
    break;

  case 28:
#line 79 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node('>', (yyvsp[-2]), (yyvsp[0])); }
#line 1580 "y.tab.c" /* yacc.c:1646  */
    break;

  case 29:
#line 81 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node(LE_OP, (yyvsp[-2]), (yyvsp[0])); }
#line 1587 "y.tab.c" /* yacc.c:1646  */
    break;

  case 30:
#line 83 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node(GE_OP, (yyvsp[-2]), (yyvsp[0])); }
#line 1594 "y.tab.c" /* yacc.c:1646  */
    break;

  case 31:
#line 88 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1600 "y.tab.c" /* yacc.c:1646  */
    break;

  case 32:
#line 89 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node(EQ_OP, (yyvsp[-2]), (yyvsp[0])); }
#line 1607 "y.tab.c" /* yacc.c:1646  */
    break;

  case 33:
#line 91 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node(NE_OP, (yyvsp[-2]), (yyvsp[0])); }
#line 1614 "y.tab.c" /* yacc.c:1646  */
    break;

  case 34:
#line 96 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1620 "y.tab.c" /* yacc.c:1646  */
    break;

  case 35:
#line 97 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node('=', (yyvsp[-2]), (yyvsp[0])); }
#line 1627 "y.tab.c" /* yacc.c:1646  */
    break;

  case 36:
#line 102 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1633 "y.tab.c" /* yacc.c:1646  */
    break;

  case 37:
#line 103 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(',', (yyvsp[-2]), (yyvsp[0])); }
#line 1639 "y.tab.c" /* yacc.c:1646  */
    break;

  case 38:
#line 107 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[-1]); }
#line 1645 "y.tab.c" /* yacc.c:1646  */
    break;

  case 39:
#line 108 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1651 "y.tab.c" /* yacc.c:1646  */
    break;

  case 40:
#line 109 "C.y" /* yacc.c:1646  */
    {
                                                  (yyval) = make_node('~', (yyvsp[-2]), (yyvsp[-1])); }
#line 1658 "y.tab.c" /* yacc.c:1646  */
    break;

  case 41:
#line 114 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1664 "y.tab.c" /* yacc.c:1646  */
    break;

  case 42:
#line 115 "C.y" /* yacc.c:1646  */
    {
                                                  (yyval) = make_node('~', (yyvsp[-1]), (yyvsp[0])); }
#line 1671 "y.tab.c" /* yacc.c:1646  */
    break;

  case 43:
#line 117 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1677 "y.tab.c" /* yacc.c:1646  */
    break;

  case 44:
#line 118 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('~', (yyvsp[-1]), (yyvsp[0])); }
#line 1683 "y.tab.c" /* yacc.c:1646  */
    break;

  case 45:
#line 122 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1689 "y.tab.c" /* yacc.c:1646  */
    break;

  case 46:
#line 123 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(',', (yyvsp[-2]), (yyvsp[0])); }
#line 1695 "y.tab.c" /* yacc.c:1646  */
    break;

  case 47:
#line 127 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1701 "y.tab.c" /* yacc.c:1646  */
    break;

  case 48:
#line 128 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('=', (yyvsp[-2]), (yyvsp[0])); }
#line 1707 "y.tab.c" /* yacc.c:1646  */
    break;

  case 49:
#line 132 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('e', (yyvsp[0]), NULL); }
#line 1713 "y.tab.c" /* yacc.c:1646  */
    break;

  case 50:
#line 133 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1719 "y.tab.c" /* yacc.c:1646  */
    break;

  case 51:
#line 137 "C.y" /* yacc.c:1646  */
    { (yyval) = make_leaf(void_token); }
#line 1725 "y.tab.c" /* yacc.c:1646  */
    break;

  case 52:
#line 138 "C.y" /* yacc.c:1646  */
    { (yyval) = make_leaf(int_token); }
#line 1731 "y.tab.c" /* yacc.c:1646  */
    break;

  case 53:
#line 139 "C.y" /* yacc.c:1646  */
    { (yyval) = make_leaf(function_token); }
#line 1737 "y.tab.c" /* yacc.c:1646  */
    break;

  case 54:
#line 143 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('~', (yyvsp[-1]), (yyvsp[0])); }
#line 1743 "y.tab.c" /* yacc.c:1646  */
    break;

  case 55:
#line 144 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1749 "y.tab.c" /* yacc.c:1646  */
    break;

  case 56:
#line 148 "C.y" /* yacc.c:1646  */
    { (yyval) = make_leaf(lasttok); }
#line 1755 "y.tab.c" /* yacc.c:1646  */
    break;

  case 57:
#line 149 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[-1]); }
#line 1761 "y.tab.c" /* yacc.c:1646  */
    break;

  case 58:
#line 150 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('F', (yyvsp[-3]), (yyvsp[-1])); }
#line 1767 "y.tab.c" /* yacc.c:1646  */
    break;

  case 59:
#line 151 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('F', (yyvsp[-3]), (yyvsp[-1])); }
#line 1773 "y.tab.c" /* yacc.c:1646  */
    break;

  case 60:
#line 152 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('F', (yyvsp[-2]), NULL); }
#line 1779 "y.tab.c" /* yacc.c:1646  */
    break;

  case 61:
#line 156 "C.y" /* yacc.c:1646  */
    { (yyval) = (NODE*)1; }
#line 1785 "y.tab.c" /* yacc.c:1646  */
    break;

  case 62:
#line 157 "C.y" /* yacc.c:1646  */
    { (yyval) = (NODE*)((int)(yyvsp[0])+1); }
#line 1791 "y.tab.c" /* yacc.c:1646  */
    break;

  case 63:
#line 161 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1797 "y.tab.c" /* yacc.c:1646  */
    break;

  case 64:
#line 162 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(',', (yyvsp[-2]), (yyvsp[0])); }
#line 1803 "y.tab.c" /* yacc.c:1646  */
    break;

  case 65:
#line 166 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('~', (yyvsp[-1]), (yyvsp[0])); }
#line 1809 "y.tab.c" /* yacc.c:1646  */
    break;

  case 66:
#line 167 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('~', (yyvsp[-1]), (yyvsp[0])); }
#line 1815 "y.tab.c" /* yacc.c:1646  */
    break;

  case 67:
#line 168 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1821 "y.tab.c" /* yacc.c:1646  */
    break;

  case 68:
#line 172 "C.y" /* yacc.c:1646  */
    { (yyval) = make_leaf(lasttok); }
#line 1827 "y.tab.c" /* yacc.c:1646  */
    break;

  case 69:
#line 173 "C.y" /* yacc.c:1646  */
    {
                                          (yyval) = make_node(',', (yyvsp[-2]),
                                                              make_leaf(lasttok)); }
#line 1835 "y.tab.c" /* yacc.c:1646  */
    break;

  case 70:
#line 179 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1841 "y.tab.c" /* yacc.c:1646  */
    break;

  case 71:
#line 180 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1847 "y.tab.c" /* yacc.c:1646  */
    break;

  case 72:
#line 181 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('G', (yyvsp[-1]), (yyvsp[0])); }
#line 1853 "y.tab.c" /* yacc.c:1646  */
    break;

  case 73:
#line 185 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[-1]); }
#line 1859 "y.tab.c" /* yacc.c:1646  */
    break;

  case 74:
#line 186 "C.y" /* yacc.c:1646  */
    { (yyval) = NULL; }
#line 1865 "y.tab.c" /* yacc.c:1646  */
    break;

  case 75:
#line 187 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[-1]); }
#line 1871 "y.tab.c" /* yacc.c:1646  */
    break;

  case 76:
#line 188 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(APPLY, (yyvsp[-2]), NULL); }
#line 1877 "y.tab.c" /* yacc.c:1646  */
    break;

  case 77:
#line 189 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(APPLY, (yyvsp[-3]), (yyvsp[-1])); }
#line 1883 "y.tab.c" /* yacc.c:1646  */
    break;

  case 78:
#line 193 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1889 "y.tab.c" /* yacc.c:1646  */
    break;

  case 79:
#line 194 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1895 "y.tab.c" /* yacc.c:1646  */
    break;

  case 80:
#line 195 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1901 "y.tab.c" /* yacc.c:1646  */
    break;

  case 81:
#line 196 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1907 "y.tab.c" /* yacc.c:1646  */
    break;

  case 82:
#line 197 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1913 "y.tab.c" /* yacc.c:1646  */
    break;

  case 83:
#line 201 "C.y" /* yacc.c:1646  */
    { (yyval) = NULL; }
#line 1919 "y.tab.c" /* yacc.c:1646  */
    break;

  case 84:
#line 202 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[-1]); }
#line 1925 "y.tab.c" /* yacc.c:1646  */
    break;

  case 85:
#line 203 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[-1]); }
#line 1931 "y.tab.c" /* yacc.c:1646  */
    break;

  case 86:
#line 204 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(';', (yyvsp[-2]), (yyvsp[-1])); }
#line 1937 "y.tab.c" /* yacc.c:1646  */
    break;

  case 87:
#line 208 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1943 "y.tab.c" /* yacc.c:1646  */
    break;

  case 88:
#line 209 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(';', (yyvsp[-1]), (yyvsp[0])); }
#line 1949 "y.tab.c" /* yacc.c:1646  */
    break;

  case 89:
#line 213 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 1955 "y.tab.c" /* yacc.c:1646  */
    break;

  case 90:
#line 214 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(';', (yyvsp[-1]), (yyvsp[0])); }
#line 1961 "y.tab.c" /* yacc.c:1646  */
    break;

  case 91:
#line 218 "C.y" /* yacc.c:1646  */
    { (yyval) = NULL; }
#line 1967 "y.tab.c" /* yacc.c:1646  */
    break;

  case 92:
#line 219 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[-1]); }
#line 1973 "y.tab.c" /* yacc.c:1646  */
    break;

  case 93:
#line 223 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(IF, (yyvsp[-2]), (yyvsp[0])); }
#line 1979 "y.tab.c" /* yacc.c:1646  */
    break;

  case 94:
#line 225 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(IF, (yyvsp[-4]),
                                                        make_node(ELSE, (yyvsp[-2]), (yyvsp[0]))); }
#line 1986 "y.tab.c" /* yacc.c:1646  */
    break;

  case 95:
#line 230 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(WHILE, (yyvsp[-2]), (yyvsp[0])); }
#line 1992 "y.tab.c" /* yacc.c:1646  */
    break;

  case 96:
#line 234 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(CONTINUE, NULL, NULL); }
#line 1998 "y.tab.c" /* yacc.c:1646  */
    break;

  case 97:
#line 235 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(BREAK, NULL, NULL); }
#line 2004 "y.tab.c" /* yacc.c:1646  */
    break;

  case 98:
#line 236 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(RETURN, NULL, NULL); }
#line 2010 "y.tab.c" /* yacc.c:1646  */
    break;

  case 99:
#line 237 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node(RETURN, (yyvsp[-1]), NULL); }
#line 2016 "y.tab.c" /* yacc.c:1646  */
    break;

  case 100:
#line 241 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 2022 "y.tab.c" /* yacc.c:1646  */
    break;

  case 101:
#line 242 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('~', (yyvsp[-1]), (yyvsp[0]));}
#line 2028 "y.tab.c" /* yacc.c:1646  */
    break;

  case 102:
#line 246 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 2034 "y.tab.c" /* yacc.c:1646  */
    break;

  case 103:
#line 247 "C.y" /* yacc.c:1646  */
    { (yyval) = (yyvsp[0]); }
#line 2040 "y.tab.c" /* yacc.c:1646  */
    break;

  case 104:
#line 251 "C.y" /* yacc.c:1646  */
    {
          (yyval) = make_node('D', make_node('d', (yyvsp[-3]), make_node('e', (yyvsp[-2]), (yyvsp[-1]))), (yyvsp[0])); }
#line 2047 "y.tab.c" /* yacc.c:1646  */
    break;

  case 105:
#line 253 "C.y" /* yacc.c:1646  */
    {
          (yyval) = make_node('D', make_node('d', (yyvsp[-2]), (yyvsp[-1])), (yyvsp[0])); }
#line 2054 "y.tab.c" /* yacc.c:1646  */
    break;

  case 106:
#line 255 "C.y" /* yacc.c:1646  */
    {
          (yyval) = make_node('D', make_node('d', (yyvsp[-2]), (yyvsp[-1])), (yyvsp[0])); }
#line 2061 "y.tab.c" /* yacc.c:1646  */
    break;

  case 107:
#line 257 "C.y" /* yacc.c:1646  */
    { (yyval) = make_node('D', (yyvsp[-1]), (yyvsp[0])); }
#line 2067 "y.tab.c" /* yacc.c:1646  */
    break;


#line 2071 "y.tab.c" /* yacc.c:1646  */
      default: break;
    }
  /* User semantic actions sometimes alter yychar, and that requires
     that yytoken be updated with the new translation.  We take the
     approach of translating immediately before every use of yytoken.
     One alternative is translating here after every semantic action,
     but that translation would be missed if the semantic action invokes
     YYABORT, YYACCEPT, or YYERROR immediately after altering yychar or
     if it invokes YYBACKUP.  In the case of YYABORT or YYACCEPT, an
     incorrect destructor might then be invoked immediately.  In the
     case of YYERROR or YYBACKUP, subsequent parser actions might lead
     to an incorrect destructor call or verbose syntax error message
     before the lookahead is translated.  */
  YY_SYMBOL_PRINT ("-> $$ =", yyr1[yyn], &yyval, &yyloc);

  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);

  *++yyvsp = yyval;

  /* Now 'shift' the result of the reduction.  Determine what state
     that goes to, based on the state we popped back to and the rule
     number reduced by.  */

  yyn = yyr1[yyn];

  yystate = yypgoto[yyn - YYNTOKENS] + *yyssp;
  if (0 <= yystate && yystate <= YYLAST && yycheck[yystate] == *yyssp)
    yystate = yytable[yystate];
  else
    yystate = yydefgoto[yyn - YYNTOKENS];

  goto yynewstate;


/*--------------------------------------.
| yyerrlab -- here on detecting error.  |
`--------------------------------------*/
yyerrlab:
  /* Make sure we have latest lookahead translation.  See comments at
     user semantic actions for why this is necessary.  */
  yytoken = yychar == YYEMPTY ? YYEMPTY : YYTRANSLATE (yychar);

  /* If not already recovering from an error, report this error.  */
  if (!yyerrstatus)
    {
      ++yynerrs;
#if ! YYERROR_VERBOSE
      yyerror (YY_("syntax error"));
#else
# define YYSYNTAX_ERROR yysyntax_error (&yymsg_alloc, &yymsg, \
                                        yyssp, yytoken)
      {
        char const *yymsgp = YY_("syntax error");
        int yysyntax_error_status;
        yysyntax_error_status = YYSYNTAX_ERROR;
        if (yysyntax_error_status == 0)
          yymsgp = yymsg;
        else if (yysyntax_error_status == 1)
          {
            if (yymsg != yymsgbuf)
              YYSTACK_FREE (yymsg);
            yymsg = (char *) YYSTACK_ALLOC (yymsg_alloc);
            if (!yymsg)
              {
                yymsg = yymsgbuf;
                yymsg_alloc = sizeof yymsgbuf;
                yysyntax_error_status = 2;
              }
            else
              {
                yysyntax_error_status = YYSYNTAX_ERROR;
                yymsgp = yymsg;
              }
          }
        yyerror (yymsgp);
        if (yysyntax_error_status == 2)
          goto yyexhaustedlab;
      }
# undef YYSYNTAX_ERROR
#endif
    }



  if (yyerrstatus == 3)
    {
      /* If just tried and failed to reuse lookahead token after an
         error, discard it.  */

      if (yychar <= YYEOF)
        {
          /* Return failure if at end of input.  */
          if (yychar == YYEOF)
            YYABORT;
        }
      else
        {
          yydestruct ("Error: discarding",
                      yytoken, &yylval);
          yychar = YYEMPTY;
        }
    }

  /* Else will try to reuse lookahead token after shifting the error
     token.  */
  goto yyerrlab1;


/*---------------------------------------------------.
| yyerrorlab -- error raised explicitly by YYERROR.  |
`---------------------------------------------------*/
yyerrorlab:

  /* Pacify compilers like GCC when the user code never invokes
     YYERROR and the label yyerrorlab therefore never appears in user
     code.  */
  if (/*CONSTCOND*/ 0)
     goto yyerrorlab;

  /* Do not reclaim the symbols of the rule whose action triggered
     this YYERROR.  */
  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);
  yystate = *yyssp;
  goto yyerrlab1;


/*-------------------------------------------------------------.
| yyerrlab1 -- common code for both syntax error and YYERROR.  |
`-------------------------------------------------------------*/
yyerrlab1:
  yyerrstatus = 3;      /* Each real token shifted decrements this.  */

  for (;;)
    {
      yyn = yypact[yystate];
      if (!yypact_value_is_default (yyn))
        {
          yyn += YYTERROR;
          if (0 <= yyn && yyn <= YYLAST && yycheck[yyn] == YYTERROR)
            {
              yyn = yytable[yyn];
              if (0 < yyn)
                break;
            }
        }

      /* Pop the current state because it cannot handle the error token.  */
      if (yyssp == yyss)
        YYABORT;


      yydestruct ("Error: popping",
                  yystos[yystate], yyvsp);
      YYPOPSTACK (1);
      yystate = *yyssp;
      YY_STACK_PRINT (yyss, yyssp);
    }

  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  *++yyvsp = yylval;
  YY_IGNORE_MAYBE_UNINITIALIZED_END


  /* Shift the error token.  */
  YY_SYMBOL_PRINT ("Shifting", yystos[yyn], yyvsp, yylsp);

  yystate = yyn;
  goto yynewstate;


/*-------------------------------------.
| yyacceptlab -- YYACCEPT comes here.  |
`-------------------------------------*/
yyacceptlab:
  yyresult = 0;
  goto yyreturn;

/*-----------------------------------.
| yyabortlab -- YYABORT comes here.  |
`-----------------------------------*/
yyabortlab:
  yyresult = 1;
  goto yyreturn;

#if !defined yyoverflow || YYERROR_VERBOSE
/*-------------------------------------------------.
| yyexhaustedlab -- memory exhaustion comes here.  |
`-------------------------------------------------*/
yyexhaustedlab:
  yyerror (YY_("memory exhausted"));
  yyresult = 2;
  /* Fall through.  */
#endif

yyreturn:
  if (yychar != YYEMPTY)
    {
      /* Make sure we have latest lookahead translation.  See comments at
         user semantic actions for why this is necessary.  */
      yytoken = YYTRANSLATE (yychar);
      yydestruct ("Cleanup: discarding lookahead",
                  yytoken, &yylval);
    }
  /* Do not reclaim the symbols of the rule whose action triggered
     this YYABORT or YYACCEPT.  */
  YYPOPSTACK (yylen);
  YY_STACK_PRINT (yyss, yyssp);
  while (yyssp != yyss)
    {
      yydestruct ("Cleanup: popping",
                  yystos[*yyssp], yyvsp);
      YYPOPSTACK (1);
    }
#ifndef yyoverflow
  if (yyss != yyssa)
    YYSTACK_FREE (yyss);
#endif
#if YYERROR_VERBOSE
  if (yymsg != yymsgbuf)
    YYSTACK_FREE (yymsg);
#endif
  return yyresult;
}
#line 259 "C.y" /* yacc.c:1906  */

#include <stdio.h>

extern char yytext[];
extern int column;

int yyerror(char *s)
{
	fflush(stdout);
	printf("\n%*s\n%*s\n", column, "^", column, s);
}
