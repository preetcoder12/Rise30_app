
/**
 * Client
**/

import * as runtime from './runtime/library.js';
import $Types = runtime.Types // general types
import $Public = runtime.Types.Public
import $Utils = runtime.Types.Utils
import $Extensions = runtime.Types.Extensions
import $Result = runtime.Types.Result

export type PrismaPromise<T> = $Public.PrismaPromise<T>


/**
 * Model User
 * 
 */
export type User = $Result.DefaultSelection<Prisma.$UserPayload>
/**
 * Model Challenge
 * 
 */
export type Challenge = $Result.DefaultSelection<Prisma.$ChallengePayload>
/**
 * Model WaterEntry
 * 
 */
export type WaterEntry = $Result.DefaultSelection<Prisma.$WaterEntryPayload>
/**
 * Model DailyEntry
 * 
 */
export type DailyEntry = $Result.DefaultSelection<Prisma.$DailyEntryPayload>
/**
 * Model Streak
 * 
 */
export type Streak = $Result.DefaultSelection<Prisma.$StreakPayload>

/**
 * ##  Prisma Client ʲˢ
 * 
 * Type-safe database client for TypeScript & Node.js
 * @example
 * ```
 * const prisma = new PrismaClient()
 * // Fetch zero or more Users
 * const users = await prisma.user.findMany()
 * ```
 *
 * 
 * Read more in our [docs](https://www.prisma.io/docs/reference/tools-and-interfaces/prisma-client).
 */
export class PrismaClient<
  ClientOptions extends Prisma.PrismaClientOptions = Prisma.PrismaClientOptions,
  U = 'log' extends keyof ClientOptions ? ClientOptions['log'] extends Array<Prisma.LogLevel | Prisma.LogDefinition> ? Prisma.GetEvents<ClientOptions['log']> : never : never,
  ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs
> {
  [K: symbol]: { types: Prisma.TypeMap<ExtArgs>['other'] }

    /**
   * ##  Prisma Client ʲˢ
   * 
   * Type-safe database client for TypeScript & Node.js
   * @example
   * ```
   * const prisma = new PrismaClient()
   * // Fetch zero or more Users
   * const users = await prisma.user.findMany()
   * ```
   *
   * 
   * Read more in our [docs](https://www.prisma.io/docs/reference/tools-and-interfaces/prisma-client).
   */

  constructor(optionsArg ?: Prisma.Subset<ClientOptions, Prisma.PrismaClientOptions>);
  $on<V extends U>(eventType: V, callback: (event: V extends 'query' ? Prisma.QueryEvent : Prisma.LogEvent) => void): void;

  /**
   * Connect with the database
   */
  $connect(): $Utils.JsPromise<void>;

  /**
   * Disconnect from the database
   */
  $disconnect(): $Utils.JsPromise<void>;

  /**
   * Add a middleware
   * @deprecated since 4.16.0. For new code, prefer client extensions instead.
   * @see https://pris.ly/d/extensions
   */
  $use(cb: Prisma.Middleware): void

/**
   * Executes a prepared raw query and returns the number of affected rows.
   * @example
   * ```
   * const result = await prisma.$executeRaw`UPDATE User SET cool = ${true} WHERE email = ${'user@email.com'};`
   * ```
   * 
   * Read more in our [docs](https://www.prisma.io/docs/reference/tools-and-interfaces/prisma-client/raw-database-access).
   */
  $executeRaw<T = unknown>(query: TemplateStringsArray | Prisma.Sql, ...values: any[]): Prisma.PrismaPromise<number>;

  /**
   * Executes a raw query and returns the number of affected rows.
   * Susceptible to SQL injections, see documentation.
   * @example
   * ```
   * const result = await prisma.$executeRawUnsafe('UPDATE User SET cool = $1 WHERE email = $2 ;', true, 'user@email.com')
   * ```
   * 
   * Read more in our [docs](https://www.prisma.io/docs/reference/tools-and-interfaces/prisma-client/raw-database-access).
   */
  $executeRawUnsafe<T = unknown>(query: string, ...values: any[]): Prisma.PrismaPromise<number>;

  /**
   * Performs a prepared raw query and returns the `SELECT` data.
   * @example
   * ```
   * const result = await prisma.$queryRaw`SELECT * FROM User WHERE id = ${1} OR email = ${'user@email.com'};`
   * ```
   * 
   * Read more in our [docs](https://www.prisma.io/docs/reference/tools-and-interfaces/prisma-client/raw-database-access).
   */
  $queryRaw<T = unknown>(query: TemplateStringsArray | Prisma.Sql, ...values: any[]): Prisma.PrismaPromise<T>;

  /**
   * Performs a raw query and returns the `SELECT` data.
   * Susceptible to SQL injections, see documentation.
   * @example
   * ```
   * const result = await prisma.$queryRawUnsafe('SELECT * FROM User WHERE id = $1 OR email = $2;', 1, 'user@email.com')
   * ```
   * 
   * Read more in our [docs](https://www.prisma.io/docs/reference/tools-and-interfaces/prisma-client/raw-database-access).
   */
  $queryRawUnsafe<T = unknown>(query: string, ...values: any[]): Prisma.PrismaPromise<T>;


  /**
   * Allows the running of a sequence of read/write operations that are guaranteed to either succeed or fail as a whole.
   * @example
   * ```
   * const [george, bob, alice] = await prisma.$transaction([
   *   prisma.user.create({ data: { name: 'George' } }),
   *   prisma.user.create({ data: { name: 'Bob' } }),
   *   prisma.user.create({ data: { name: 'Alice' } }),
   * ])
   * ```
   * 
   * Read more in our [docs](https://www.prisma.io/docs/concepts/components/prisma-client/transactions).
   */
  $transaction<P extends Prisma.PrismaPromise<any>[]>(arg: [...P], options?: { isolationLevel?: Prisma.TransactionIsolationLevel }): $Utils.JsPromise<runtime.Types.Utils.UnwrapTuple<P>>

  $transaction<R>(fn: (prisma: Omit<PrismaClient, runtime.ITXClientDenyList>) => $Utils.JsPromise<R>, options?: { maxWait?: number, timeout?: number, isolationLevel?: Prisma.TransactionIsolationLevel }): $Utils.JsPromise<R>


  $extends: $Extensions.ExtendsHook<"extends", Prisma.TypeMapCb, ExtArgs>

      /**
   * `prisma.user`: Exposes CRUD operations for the **User** model.
    * Example usage:
    * ```ts
    * // Fetch zero or more Users
    * const users = await prisma.user.findMany()
    * ```
    */
  get user(): Prisma.UserDelegate<ExtArgs>;

  /**
   * `prisma.challenge`: Exposes CRUD operations for the **Challenge** model.
    * Example usage:
    * ```ts
    * // Fetch zero or more Challenges
    * const challenges = await prisma.challenge.findMany()
    * ```
    */
  get challenge(): Prisma.ChallengeDelegate<ExtArgs>;

  /**
   * `prisma.waterEntry`: Exposes CRUD operations for the **WaterEntry** model.
    * Example usage:
    * ```ts
    * // Fetch zero or more WaterEntries
    * const waterEntries = await prisma.waterEntry.findMany()
    * ```
    */
  get waterEntry(): Prisma.WaterEntryDelegate<ExtArgs>;

  /**
   * `prisma.dailyEntry`: Exposes CRUD operations for the **DailyEntry** model.
    * Example usage:
    * ```ts
    * // Fetch zero or more DailyEntries
    * const dailyEntries = await prisma.dailyEntry.findMany()
    * ```
    */
  get dailyEntry(): Prisma.DailyEntryDelegate<ExtArgs>;

  /**
   * `prisma.streak`: Exposes CRUD operations for the **Streak** model.
    * Example usage:
    * ```ts
    * // Fetch zero or more Streaks
    * const streaks = await prisma.streak.findMany()
    * ```
    */
  get streak(): Prisma.StreakDelegate<ExtArgs>;
}

export namespace Prisma {
  export import DMMF = runtime.DMMF

  export type PrismaPromise<T> = $Public.PrismaPromise<T>

  /**
   * Validator
   */
  export import validator = runtime.Public.validator

  /**
   * Prisma Errors
   */
  export import PrismaClientKnownRequestError = runtime.PrismaClientKnownRequestError
  export import PrismaClientUnknownRequestError = runtime.PrismaClientUnknownRequestError
  export import PrismaClientRustPanicError = runtime.PrismaClientRustPanicError
  export import PrismaClientInitializationError = runtime.PrismaClientInitializationError
  export import PrismaClientValidationError = runtime.PrismaClientValidationError
  export import NotFoundError = runtime.NotFoundError

  /**
   * Re-export of sql-template-tag
   */
  export import sql = runtime.sqltag
  export import empty = runtime.empty
  export import join = runtime.join
  export import raw = runtime.raw
  export import Sql = runtime.Sql



  /**
   * Decimal.js
   */
  export import Decimal = runtime.Decimal

  export type DecimalJsLike = runtime.DecimalJsLike

  /**
   * Metrics 
   */
  export type Metrics = runtime.Metrics
  export type Metric<T> = runtime.Metric<T>
  export type MetricHistogram = runtime.MetricHistogram
  export type MetricHistogramBucket = runtime.MetricHistogramBucket

  /**
  * Extensions
  */
  export import Extension = $Extensions.UserArgs
  export import getExtensionContext = runtime.Extensions.getExtensionContext
  export import Args = $Public.Args
  export import Payload = $Public.Payload
  export import Result = $Public.Result
  export import Exact = $Public.Exact

  /**
   * Prisma Client JS version: 5.22.0
   * Query Engine version: 605197351a3c8bdd595af2d2a9bc3025bca48ea2
   */
  export type PrismaVersion = {
    client: string
  }

  export const prismaVersion: PrismaVersion 

  /**
   * Utility Types
   */


  export import JsonObject = runtime.JsonObject
  export import JsonArray = runtime.JsonArray
  export import JsonValue = runtime.JsonValue
  export import InputJsonObject = runtime.InputJsonObject
  export import InputJsonArray = runtime.InputJsonArray
  export import InputJsonValue = runtime.InputJsonValue

  /**
   * Types of the values used to represent different kinds of `null` values when working with JSON fields.
   * 
   * @see https://www.prisma.io/docs/concepts/components/prisma-client/working-with-fields/working-with-json-fields#filtering-on-a-json-field
   */
  namespace NullTypes {
    /**
    * Type of `Prisma.DbNull`.
    * 
    * You cannot use other instances of this class. Please use the `Prisma.DbNull` value.
    * 
    * @see https://www.prisma.io/docs/concepts/components/prisma-client/working-with-fields/working-with-json-fields#filtering-on-a-json-field
    */
    class DbNull {
      private DbNull: never
      private constructor()
    }

    /**
    * Type of `Prisma.JsonNull`.
    * 
    * You cannot use other instances of this class. Please use the `Prisma.JsonNull` value.
    * 
    * @see https://www.prisma.io/docs/concepts/components/prisma-client/working-with-fields/working-with-json-fields#filtering-on-a-json-field
    */
    class JsonNull {
      private JsonNull: never
      private constructor()
    }

    /**
    * Type of `Prisma.AnyNull`.
    * 
    * You cannot use other instances of this class. Please use the `Prisma.AnyNull` value.
    * 
    * @see https://www.prisma.io/docs/concepts/components/prisma-client/working-with-fields/working-with-json-fields#filtering-on-a-json-field
    */
    class AnyNull {
      private AnyNull: never
      private constructor()
    }
  }

  /**
   * Helper for filtering JSON entries that have `null` on the database (empty on the db)
   * 
   * @see https://www.prisma.io/docs/concepts/components/prisma-client/working-with-fields/working-with-json-fields#filtering-on-a-json-field
   */
  export const DbNull: NullTypes.DbNull

  /**
   * Helper for filtering JSON entries that have JSON `null` values (not empty on the db)
   * 
   * @see https://www.prisma.io/docs/concepts/components/prisma-client/working-with-fields/working-with-json-fields#filtering-on-a-json-field
   */
  export const JsonNull: NullTypes.JsonNull

  /**
   * Helper for filtering JSON entries that are `Prisma.DbNull` or `Prisma.JsonNull`
   * 
   * @see https://www.prisma.io/docs/concepts/components/prisma-client/working-with-fields/working-with-json-fields#filtering-on-a-json-field
   */
  export const AnyNull: NullTypes.AnyNull

  type SelectAndInclude = {
    select: any
    include: any
  }

  type SelectAndOmit = {
    select: any
    omit: any
  }

  /**
   * Get the type of the value, that the Promise holds.
   */
  export type PromiseType<T extends PromiseLike<any>> = T extends PromiseLike<infer U> ? U : T;

  /**
   * Get the return type of a function which returns a Promise.
   */
  export type PromiseReturnType<T extends (...args: any) => $Utils.JsPromise<any>> = PromiseType<ReturnType<T>>

  /**
   * From T, pick a set of properties whose keys are in the union K
   */
  type Prisma__Pick<T, K extends keyof T> = {
      [P in K]: T[P];
  };


  export type Enumerable<T> = T | Array<T>;

  export type RequiredKeys<T> = {
    [K in keyof T]-?: {} extends Prisma__Pick<T, K> ? never : K
  }[keyof T]

  export type TruthyKeys<T> = keyof {
    [K in keyof T as T[K] extends false | undefined | null ? never : K]: K
  }

  export type TrueKeys<T> = TruthyKeys<Prisma__Pick<T, RequiredKeys<T>>>

  /**
   * Subset
   * @desc From `T` pick properties that exist in `U`. Simple version of Intersection
   */
  export type Subset<T, U> = {
    [key in keyof T]: key extends keyof U ? T[key] : never;
  };

  /**
   * SelectSubset
   * @desc From `T` pick properties that exist in `U`. Simple version of Intersection.
   * Additionally, it validates, if both select and include are present. If the case, it errors.
   */
  export type SelectSubset<T, U> = {
    [key in keyof T]: key extends keyof U ? T[key] : never
  } &
    (T extends SelectAndInclude
      ? 'Please either choose `select` or `include`.'
      : T extends SelectAndOmit
        ? 'Please either choose `select` or `omit`.'
        : {})

  /**
   * Subset + Intersection
   * @desc From `T` pick properties that exist in `U` and intersect `K`
   */
  export type SubsetIntersection<T, U, K> = {
    [key in keyof T]: key extends keyof U ? T[key] : never
  } &
    K

  type Without<T, U> = { [P in Exclude<keyof T, keyof U>]?: never };

  /**
   * XOR is needed to have a real mutually exclusive union type
   * https://stackoverflow.com/questions/42123407/does-typescript-support-mutually-exclusive-types
   */
  type XOR<T, U> =
    T extends object ?
    U extends object ?
      (Without<T, U> & U) | (Without<U, T> & T)
    : U : T


  /**
   * Is T a Record?
   */
  type IsObject<T extends any> = T extends Array<any>
  ? False
  : T extends Date
  ? False
  : T extends Uint8Array
  ? False
  : T extends BigInt
  ? False
  : T extends object
  ? True
  : False


  /**
   * If it's T[], return T
   */
  export type UnEnumerate<T extends unknown> = T extends Array<infer U> ? U : T

  /**
   * From ts-toolbelt
   */

  type __Either<O extends object, K extends Key> = Omit<O, K> &
    {
      // Merge all but K
      [P in K]: Prisma__Pick<O, P & keyof O> // With K possibilities
    }[K]

  type EitherStrict<O extends object, K extends Key> = Strict<__Either<O, K>>

  type EitherLoose<O extends object, K extends Key> = ComputeRaw<__Either<O, K>>

  type _Either<
    O extends object,
    K extends Key,
    strict extends Boolean
  > = {
    1: EitherStrict<O, K>
    0: EitherLoose<O, K>
  }[strict]

  type Either<
    O extends object,
    K extends Key,
    strict extends Boolean = 1
  > = O extends unknown ? _Either<O, K, strict> : never

  export type Union = any

  type PatchUndefined<O extends object, O1 extends object> = {
    [K in keyof O]: O[K] extends undefined ? At<O1, K> : O[K]
  } & {}

  /** Helper Types for "Merge" **/
  export type IntersectOf<U extends Union> = (
    U extends unknown ? (k: U) => void : never
  ) extends (k: infer I) => void
    ? I
    : never

  export type Overwrite<O extends object, O1 extends object> = {
      [K in keyof O]: K extends keyof O1 ? O1[K] : O[K];
  } & {};

  type _Merge<U extends object> = IntersectOf<Overwrite<U, {
      [K in keyof U]-?: At<U, K>;
  }>>;

  type Key = string | number | symbol;
  type AtBasic<O extends object, K extends Key> = K extends keyof O ? O[K] : never;
  type AtStrict<O extends object, K extends Key> = O[K & keyof O];
  type AtLoose<O extends object, K extends Key> = O extends unknown ? AtStrict<O, K> : never;
  export type At<O extends object, K extends Key, strict extends Boolean = 1> = {
      1: AtStrict<O, K>;
      0: AtLoose<O, K>;
  }[strict];

  export type ComputeRaw<A extends any> = A extends Function ? A : {
    [K in keyof A]: A[K];
  } & {};

  export type OptionalFlat<O> = {
    [K in keyof O]?: O[K];
  } & {};

  type _Record<K extends keyof any, T> = {
    [P in K]: T;
  };

  // cause typescript not to expand types and preserve names
  type NoExpand<T> = T extends unknown ? T : never;

  // this type assumes the passed object is entirely optional
  type AtLeast<O extends object, K extends string> = NoExpand<
    O extends unknown
    ? | (K extends keyof O ? { [P in K]: O[P] } & O : O)
      | {[P in keyof O as P extends K ? K : never]-?: O[P]} & O
    : never>;

  type _Strict<U, _U = U> = U extends unknown ? U & OptionalFlat<_Record<Exclude<Keys<_U>, keyof U>, never>> : never;

  export type Strict<U extends object> = ComputeRaw<_Strict<U>>;
  /** End Helper Types for "Merge" **/

  export type Merge<U extends object> = ComputeRaw<_Merge<Strict<U>>>;

  /**
  A [[Boolean]]
  */
  export type Boolean = True | False

  // /**
  // 1
  // */
  export type True = 1

  /**
  0
  */
  export type False = 0

  export type Not<B extends Boolean> = {
    0: 1
    1: 0
  }[B]

  export type Extends<A1 extends any, A2 extends any> = [A1] extends [never]
    ? 0 // anything `never` is false
    : A1 extends A2
    ? 1
    : 0

  export type Has<U extends Union, U1 extends Union> = Not<
    Extends<Exclude<U1, U>, U1>
  >

  export type Or<B1 extends Boolean, B2 extends Boolean> = {
    0: {
      0: 0
      1: 1
    }
    1: {
      0: 1
      1: 1
    }
  }[B1][B2]

  export type Keys<U extends Union> = U extends unknown ? keyof U : never

  type Cast<A, B> = A extends B ? A : B;

  export const type: unique symbol;



  /**
   * Used by group by
   */

  export type GetScalarType<T, O> = O extends object ? {
    [P in keyof T]: P extends keyof O
      ? O[P]
      : never
  } : never

  type FieldPaths<
    T,
    U = Omit<T, '_avg' | '_sum' | '_count' | '_min' | '_max'>
  > = IsObject<T> extends True ? U : T

  type GetHavingFields<T> = {
    [K in keyof T]: Or<
      Or<Extends<'OR', K>, Extends<'AND', K>>,
      Extends<'NOT', K>
    > extends True
      ? // infer is only needed to not hit TS limit
        // based on the brilliant idea of Pierre-Antoine Mills
        // https://github.com/microsoft/TypeScript/issues/30188#issuecomment-478938437
        T[K] extends infer TK
        ? GetHavingFields<UnEnumerate<TK> extends object ? Merge<UnEnumerate<TK>> : never>
        : never
      : {} extends FieldPaths<T[K]>
      ? never
      : K
  }[keyof T]

  /**
   * Convert tuple to union
   */
  type _TupleToUnion<T> = T extends (infer E)[] ? E : never
  type TupleToUnion<K extends readonly any[]> = _TupleToUnion<K>
  type MaybeTupleToUnion<T> = T extends any[] ? TupleToUnion<T> : T

  /**
   * Like `Pick`, but additionally can also accept an array of keys
   */
  type PickEnumerable<T, K extends Enumerable<keyof T> | keyof T> = Prisma__Pick<T, MaybeTupleToUnion<K>>

  /**
   * Exclude all keys with underscores
   */
  type ExcludeUnderscoreKeys<T extends string> = T extends `_${string}` ? never : T


  export type FieldRef<Model, FieldType> = runtime.FieldRef<Model, FieldType>

  type FieldRefInputType<Model, FieldType> = Model extends never ? never : FieldRef<Model, FieldType>


  export const ModelName: {
    User: 'User',
    Challenge: 'Challenge',
    WaterEntry: 'WaterEntry',
    DailyEntry: 'DailyEntry',
    Streak: 'Streak'
  };

  export type ModelName = (typeof ModelName)[keyof typeof ModelName]


  export type Datasources = {
    db?: Datasource
  }

  interface TypeMapCb extends $Utils.Fn<{extArgs: $Extensions.InternalArgs, clientOptions: PrismaClientOptions }, $Utils.Record<string, any>> {
    returns: Prisma.TypeMap<this['params']['extArgs'], this['params']['clientOptions']>
  }

  export type TypeMap<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs, ClientOptions = {}> = {
    meta: {
      modelProps: "user" | "challenge" | "waterEntry" | "dailyEntry" | "streak"
      txIsolationLevel: Prisma.TransactionIsolationLevel
    }
    model: {
      User: {
        payload: Prisma.$UserPayload<ExtArgs>
        fields: Prisma.UserFieldRefs
        operations: {
          findUnique: {
            args: Prisma.UserFindUniqueArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$UserPayload> | null
          }
          findUniqueOrThrow: {
            args: Prisma.UserFindUniqueOrThrowArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$UserPayload>
          }
          findFirst: {
            args: Prisma.UserFindFirstArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$UserPayload> | null
          }
          findFirstOrThrow: {
            args: Prisma.UserFindFirstOrThrowArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$UserPayload>
          }
          findMany: {
            args: Prisma.UserFindManyArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$UserPayload>[]
          }
          create: {
            args: Prisma.UserCreateArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$UserPayload>
          }
          createMany: {
            args: Prisma.UserCreateManyArgs<ExtArgs>
            result: BatchPayload
          }
          createManyAndReturn: {
            args: Prisma.UserCreateManyAndReturnArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$UserPayload>[]
          }
          delete: {
            args: Prisma.UserDeleteArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$UserPayload>
          }
          update: {
            args: Prisma.UserUpdateArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$UserPayload>
          }
          deleteMany: {
            args: Prisma.UserDeleteManyArgs<ExtArgs>
            result: BatchPayload
          }
          updateMany: {
            args: Prisma.UserUpdateManyArgs<ExtArgs>
            result: BatchPayload
          }
          upsert: {
            args: Prisma.UserUpsertArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$UserPayload>
          }
          aggregate: {
            args: Prisma.UserAggregateArgs<ExtArgs>
            result: $Utils.Optional<AggregateUser>
          }
          groupBy: {
            args: Prisma.UserGroupByArgs<ExtArgs>
            result: $Utils.Optional<UserGroupByOutputType>[]
          }
          count: {
            args: Prisma.UserCountArgs<ExtArgs>
            result: $Utils.Optional<UserCountAggregateOutputType> | number
          }
        }
      }
      Challenge: {
        payload: Prisma.$ChallengePayload<ExtArgs>
        fields: Prisma.ChallengeFieldRefs
        operations: {
          findUnique: {
            args: Prisma.ChallengeFindUniqueArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$ChallengePayload> | null
          }
          findUniqueOrThrow: {
            args: Prisma.ChallengeFindUniqueOrThrowArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$ChallengePayload>
          }
          findFirst: {
            args: Prisma.ChallengeFindFirstArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$ChallengePayload> | null
          }
          findFirstOrThrow: {
            args: Prisma.ChallengeFindFirstOrThrowArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$ChallengePayload>
          }
          findMany: {
            args: Prisma.ChallengeFindManyArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$ChallengePayload>[]
          }
          create: {
            args: Prisma.ChallengeCreateArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$ChallengePayload>
          }
          createMany: {
            args: Prisma.ChallengeCreateManyArgs<ExtArgs>
            result: BatchPayload
          }
          createManyAndReturn: {
            args: Prisma.ChallengeCreateManyAndReturnArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$ChallengePayload>[]
          }
          delete: {
            args: Prisma.ChallengeDeleteArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$ChallengePayload>
          }
          update: {
            args: Prisma.ChallengeUpdateArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$ChallengePayload>
          }
          deleteMany: {
            args: Prisma.ChallengeDeleteManyArgs<ExtArgs>
            result: BatchPayload
          }
          updateMany: {
            args: Prisma.ChallengeUpdateManyArgs<ExtArgs>
            result: BatchPayload
          }
          upsert: {
            args: Prisma.ChallengeUpsertArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$ChallengePayload>
          }
          aggregate: {
            args: Prisma.ChallengeAggregateArgs<ExtArgs>
            result: $Utils.Optional<AggregateChallenge>
          }
          groupBy: {
            args: Prisma.ChallengeGroupByArgs<ExtArgs>
            result: $Utils.Optional<ChallengeGroupByOutputType>[]
          }
          count: {
            args: Prisma.ChallengeCountArgs<ExtArgs>
            result: $Utils.Optional<ChallengeCountAggregateOutputType> | number
          }
        }
      }
      WaterEntry: {
        payload: Prisma.$WaterEntryPayload<ExtArgs>
        fields: Prisma.WaterEntryFieldRefs
        operations: {
          findUnique: {
            args: Prisma.WaterEntryFindUniqueArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$WaterEntryPayload> | null
          }
          findUniqueOrThrow: {
            args: Prisma.WaterEntryFindUniqueOrThrowArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$WaterEntryPayload>
          }
          findFirst: {
            args: Prisma.WaterEntryFindFirstArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$WaterEntryPayload> | null
          }
          findFirstOrThrow: {
            args: Prisma.WaterEntryFindFirstOrThrowArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$WaterEntryPayload>
          }
          findMany: {
            args: Prisma.WaterEntryFindManyArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$WaterEntryPayload>[]
          }
          create: {
            args: Prisma.WaterEntryCreateArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$WaterEntryPayload>
          }
          createMany: {
            args: Prisma.WaterEntryCreateManyArgs<ExtArgs>
            result: BatchPayload
          }
          createManyAndReturn: {
            args: Prisma.WaterEntryCreateManyAndReturnArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$WaterEntryPayload>[]
          }
          delete: {
            args: Prisma.WaterEntryDeleteArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$WaterEntryPayload>
          }
          update: {
            args: Prisma.WaterEntryUpdateArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$WaterEntryPayload>
          }
          deleteMany: {
            args: Prisma.WaterEntryDeleteManyArgs<ExtArgs>
            result: BatchPayload
          }
          updateMany: {
            args: Prisma.WaterEntryUpdateManyArgs<ExtArgs>
            result: BatchPayload
          }
          upsert: {
            args: Prisma.WaterEntryUpsertArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$WaterEntryPayload>
          }
          aggregate: {
            args: Prisma.WaterEntryAggregateArgs<ExtArgs>
            result: $Utils.Optional<AggregateWaterEntry>
          }
          groupBy: {
            args: Prisma.WaterEntryGroupByArgs<ExtArgs>
            result: $Utils.Optional<WaterEntryGroupByOutputType>[]
          }
          count: {
            args: Prisma.WaterEntryCountArgs<ExtArgs>
            result: $Utils.Optional<WaterEntryCountAggregateOutputType> | number
          }
        }
      }
      DailyEntry: {
        payload: Prisma.$DailyEntryPayload<ExtArgs>
        fields: Prisma.DailyEntryFieldRefs
        operations: {
          findUnique: {
            args: Prisma.DailyEntryFindUniqueArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$DailyEntryPayload> | null
          }
          findUniqueOrThrow: {
            args: Prisma.DailyEntryFindUniqueOrThrowArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$DailyEntryPayload>
          }
          findFirst: {
            args: Prisma.DailyEntryFindFirstArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$DailyEntryPayload> | null
          }
          findFirstOrThrow: {
            args: Prisma.DailyEntryFindFirstOrThrowArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$DailyEntryPayload>
          }
          findMany: {
            args: Prisma.DailyEntryFindManyArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$DailyEntryPayload>[]
          }
          create: {
            args: Prisma.DailyEntryCreateArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$DailyEntryPayload>
          }
          createMany: {
            args: Prisma.DailyEntryCreateManyArgs<ExtArgs>
            result: BatchPayload
          }
          createManyAndReturn: {
            args: Prisma.DailyEntryCreateManyAndReturnArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$DailyEntryPayload>[]
          }
          delete: {
            args: Prisma.DailyEntryDeleteArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$DailyEntryPayload>
          }
          update: {
            args: Prisma.DailyEntryUpdateArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$DailyEntryPayload>
          }
          deleteMany: {
            args: Prisma.DailyEntryDeleteManyArgs<ExtArgs>
            result: BatchPayload
          }
          updateMany: {
            args: Prisma.DailyEntryUpdateManyArgs<ExtArgs>
            result: BatchPayload
          }
          upsert: {
            args: Prisma.DailyEntryUpsertArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$DailyEntryPayload>
          }
          aggregate: {
            args: Prisma.DailyEntryAggregateArgs<ExtArgs>
            result: $Utils.Optional<AggregateDailyEntry>
          }
          groupBy: {
            args: Prisma.DailyEntryGroupByArgs<ExtArgs>
            result: $Utils.Optional<DailyEntryGroupByOutputType>[]
          }
          count: {
            args: Prisma.DailyEntryCountArgs<ExtArgs>
            result: $Utils.Optional<DailyEntryCountAggregateOutputType> | number
          }
        }
      }
      Streak: {
        payload: Prisma.$StreakPayload<ExtArgs>
        fields: Prisma.StreakFieldRefs
        operations: {
          findUnique: {
            args: Prisma.StreakFindUniqueArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$StreakPayload> | null
          }
          findUniqueOrThrow: {
            args: Prisma.StreakFindUniqueOrThrowArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$StreakPayload>
          }
          findFirst: {
            args: Prisma.StreakFindFirstArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$StreakPayload> | null
          }
          findFirstOrThrow: {
            args: Prisma.StreakFindFirstOrThrowArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$StreakPayload>
          }
          findMany: {
            args: Prisma.StreakFindManyArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$StreakPayload>[]
          }
          create: {
            args: Prisma.StreakCreateArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$StreakPayload>
          }
          createMany: {
            args: Prisma.StreakCreateManyArgs<ExtArgs>
            result: BatchPayload
          }
          createManyAndReturn: {
            args: Prisma.StreakCreateManyAndReturnArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$StreakPayload>[]
          }
          delete: {
            args: Prisma.StreakDeleteArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$StreakPayload>
          }
          update: {
            args: Prisma.StreakUpdateArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$StreakPayload>
          }
          deleteMany: {
            args: Prisma.StreakDeleteManyArgs<ExtArgs>
            result: BatchPayload
          }
          updateMany: {
            args: Prisma.StreakUpdateManyArgs<ExtArgs>
            result: BatchPayload
          }
          upsert: {
            args: Prisma.StreakUpsertArgs<ExtArgs>
            result: $Utils.PayloadToResult<Prisma.$StreakPayload>
          }
          aggregate: {
            args: Prisma.StreakAggregateArgs<ExtArgs>
            result: $Utils.Optional<AggregateStreak>
          }
          groupBy: {
            args: Prisma.StreakGroupByArgs<ExtArgs>
            result: $Utils.Optional<StreakGroupByOutputType>[]
          }
          count: {
            args: Prisma.StreakCountArgs<ExtArgs>
            result: $Utils.Optional<StreakCountAggregateOutputType> | number
          }
        }
      }
    }
  } & {
    other: {
      payload: any
      operations: {
        $executeRaw: {
          args: [query: TemplateStringsArray | Prisma.Sql, ...values: any[]],
          result: any
        }
        $executeRawUnsafe: {
          args: [query: string, ...values: any[]],
          result: any
        }
        $queryRaw: {
          args: [query: TemplateStringsArray | Prisma.Sql, ...values: any[]],
          result: any
        }
        $queryRawUnsafe: {
          args: [query: string, ...values: any[]],
          result: any
        }
      }
    }
  }
  export const defineExtension: $Extensions.ExtendsHook<"define", Prisma.TypeMapCb, $Extensions.DefaultArgs>
  export type DefaultPrismaClient = PrismaClient
  export type ErrorFormat = 'pretty' | 'colorless' | 'minimal'
  export interface PrismaClientOptions {
    /**
     * Overwrites the datasource url from your schema.prisma file
     */
    datasources?: Datasources
    /**
     * Overwrites the datasource url from your schema.prisma file
     */
    datasourceUrl?: string
    /**
     * @default "colorless"
     */
    errorFormat?: ErrorFormat
    /**
     * @example
     * ```
     * // Defaults to stdout
     * log: ['query', 'info', 'warn', 'error']
     * 
     * // Emit as events
     * log: [
     *   { emit: 'stdout', level: 'query' },
     *   { emit: 'stdout', level: 'info' },
     *   { emit: 'stdout', level: 'warn' }
     *   { emit: 'stdout', level: 'error' }
     * ]
     * ```
     * Read more in our [docs](https://www.prisma.io/docs/reference/tools-and-interfaces/prisma-client/logging#the-log-option).
     */
    log?: (LogLevel | LogDefinition)[]
    /**
     * The default values for transactionOptions
     * maxWait ?= 2000
     * timeout ?= 5000
     */
    transactionOptions?: {
      maxWait?: number
      timeout?: number
      isolationLevel?: Prisma.TransactionIsolationLevel
    }
  }


  /* Types for Logging */
  export type LogLevel = 'info' | 'query' | 'warn' | 'error'
  export type LogDefinition = {
    level: LogLevel
    emit: 'stdout' | 'event'
  }

  export type GetLogType<T extends LogLevel | LogDefinition> = T extends LogDefinition ? T['emit'] extends 'event' ? T['level'] : never : never
  export type GetEvents<T extends any> = T extends Array<LogLevel | LogDefinition> ?
    GetLogType<T[0]> | GetLogType<T[1]> | GetLogType<T[2]> | GetLogType<T[3]>
    : never

  export type QueryEvent = {
    timestamp: Date
    query: string
    params: string
    duration: number
    target: string
  }

  export type LogEvent = {
    timestamp: Date
    message: string
    target: string
  }
  /* End Types for Logging */


  export type PrismaAction =
    | 'findUnique'
    | 'findUniqueOrThrow'
    | 'findMany'
    | 'findFirst'
    | 'findFirstOrThrow'
    | 'create'
    | 'createMany'
    | 'createManyAndReturn'
    | 'update'
    | 'updateMany'
    | 'upsert'
    | 'delete'
    | 'deleteMany'
    | 'executeRaw'
    | 'queryRaw'
    | 'aggregate'
    | 'count'
    | 'runCommandRaw'
    | 'findRaw'
    | 'groupBy'

  /**
   * These options are being passed into the middleware as "params"
   */
  export type MiddlewareParams = {
    model?: ModelName
    action: PrismaAction
    args: any
    dataPath: string[]
    runInTransaction: boolean
  }

  /**
   * The `T` type makes sure, that the `return proceed` is not forgotten in the middleware implementation
   */
  export type Middleware<T = any> = (
    params: MiddlewareParams,
    next: (params: MiddlewareParams) => $Utils.JsPromise<T>,
  ) => $Utils.JsPromise<T>

  // tested in getLogLevel.test.ts
  export function getLogLevel(log: Array<LogLevel | LogDefinition>): LogLevel | undefined;

  /**
   * `PrismaClient` proxy available in interactive transactions.
   */
  export type TransactionClient = Omit<Prisma.DefaultPrismaClient, runtime.ITXClientDenyList>

  export type Datasource = {
    url?: string
  }

  /**
   * Count Types
   */


  /**
   * Count Type UserCountOutputType
   */

  export type UserCountOutputType = {
    challenges: number
    streaks: number
    dailyEntries: number
    waterEntries: number
  }

  export type UserCountOutputTypeSelect<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    challenges?: boolean | UserCountOutputTypeCountChallengesArgs
    streaks?: boolean | UserCountOutputTypeCountStreaksArgs
    dailyEntries?: boolean | UserCountOutputTypeCountDailyEntriesArgs
    waterEntries?: boolean | UserCountOutputTypeCountWaterEntriesArgs
  }

  // Custom InputTypes
  /**
   * UserCountOutputType without action
   */
  export type UserCountOutputTypeDefaultArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the UserCountOutputType
     */
    select?: UserCountOutputTypeSelect<ExtArgs> | null
  }

  /**
   * UserCountOutputType without action
   */
  export type UserCountOutputTypeCountChallengesArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: ChallengeWhereInput
  }

  /**
   * UserCountOutputType without action
   */
  export type UserCountOutputTypeCountStreaksArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: StreakWhereInput
  }

  /**
   * UserCountOutputType without action
   */
  export type UserCountOutputTypeCountDailyEntriesArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: DailyEntryWhereInput
  }

  /**
   * UserCountOutputType without action
   */
  export type UserCountOutputTypeCountWaterEntriesArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: WaterEntryWhereInput
  }


  /**
   * Count Type ChallengeCountOutputType
   */

  export type ChallengeCountOutputType = {
    dailyTasks: number
    streaks: number
    waterEntries: number
  }

  export type ChallengeCountOutputTypeSelect<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    dailyTasks?: boolean | ChallengeCountOutputTypeCountDailyTasksArgs
    streaks?: boolean | ChallengeCountOutputTypeCountStreaksArgs
    waterEntries?: boolean | ChallengeCountOutputTypeCountWaterEntriesArgs
  }

  // Custom InputTypes
  /**
   * ChallengeCountOutputType without action
   */
  export type ChallengeCountOutputTypeDefaultArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the ChallengeCountOutputType
     */
    select?: ChallengeCountOutputTypeSelect<ExtArgs> | null
  }

  /**
   * ChallengeCountOutputType without action
   */
  export type ChallengeCountOutputTypeCountDailyTasksArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: DailyEntryWhereInput
  }

  /**
   * ChallengeCountOutputType without action
   */
  export type ChallengeCountOutputTypeCountStreaksArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: StreakWhereInput
  }

  /**
   * ChallengeCountOutputType without action
   */
  export type ChallengeCountOutputTypeCountWaterEntriesArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: WaterEntryWhereInput
  }


  /**
   * Models
   */

  /**
   * Model User
   */

  export type AggregateUser = {
    _count: UserCountAggregateOutputType | null
    _min: UserMinAggregateOutputType | null
    _max: UserMaxAggregateOutputType | null
  }

  export type UserMinAggregateOutputType = {
    id: string | null
    email: string | null
    createdAt: Date | null
  }

  export type UserMaxAggregateOutputType = {
    id: string | null
    email: string | null
    createdAt: Date | null
  }

  export type UserCountAggregateOutputType = {
    id: number
    email: number
    createdAt: number
    _all: number
  }


  export type UserMinAggregateInputType = {
    id?: true
    email?: true
    createdAt?: true
  }

  export type UserMaxAggregateInputType = {
    id?: true
    email?: true
    createdAt?: true
  }

  export type UserCountAggregateInputType = {
    id?: true
    email?: true
    createdAt?: true
    _all?: true
  }

  export type UserAggregateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Filter which User to aggregate.
     */
    where?: UserWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Users to fetch.
     */
    orderBy?: UserOrderByWithRelationInput | UserOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the start position
     */
    cursor?: UserWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Users from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Users.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Count returned Users
    **/
    _count?: true | UserCountAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to find the minimum value
    **/
    _min?: UserMinAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to find the maximum value
    **/
    _max?: UserMaxAggregateInputType
  }

  export type GetUserAggregateType<T extends UserAggregateArgs> = {
        [P in keyof T & keyof AggregateUser]: P extends '_count' | 'count'
      ? T[P] extends true
        ? number
        : GetScalarType<T[P], AggregateUser[P]>
      : GetScalarType<T[P], AggregateUser[P]>
  }




  export type UserGroupByArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: UserWhereInput
    orderBy?: UserOrderByWithAggregationInput | UserOrderByWithAggregationInput[]
    by: UserScalarFieldEnum[] | UserScalarFieldEnum
    having?: UserScalarWhereWithAggregatesInput
    take?: number
    skip?: number
    _count?: UserCountAggregateInputType | true
    _min?: UserMinAggregateInputType
    _max?: UserMaxAggregateInputType
  }

  export type UserGroupByOutputType = {
    id: string
    email: string
    createdAt: Date
    _count: UserCountAggregateOutputType | null
    _min: UserMinAggregateOutputType | null
    _max: UserMaxAggregateOutputType | null
  }

  type GetUserGroupByPayload<T extends UserGroupByArgs> = Prisma.PrismaPromise<
    Array<
      PickEnumerable<UserGroupByOutputType, T['by']> &
        {
          [P in ((keyof T) & (keyof UserGroupByOutputType))]: P extends '_count'
            ? T[P] extends boolean
              ? number
              : GetScalarType<T[P], UserGroupByOutputType[P]>
            : GetScalarType<T[P], UserGroupByOutputType[P]>
        }
      >
    >


  export type UserSelect<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = $Extensions.GetSelect<{
    id?: boolean
    email?: boolean
    createdAt?: boolean
    challenges?: boolean | User$challengesArgs<ExtArgs>
    streaks?: boolean | User$streaksArgs<ExtArgs>
    dailyEntries?: boolean | User$dailyEntriesArgs<ExtArgs>
    waterEntries?: boolean | User$waterEntriesArgs<ExtArgs>
    _count?: boolean | UserCountOutputTypeDefaultArgs<ExtArgs>
  }, ExtArgs["result"]["user"]>

  export type UserSelectCreateManyAndReturn<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = $Extensions.GetSelect<{
    id?: boolean
    email?: boolean
    createdAt?: boolean
  }, ExtArgs["result"]["user"]>

  export type UserSelectScalar = {
    id?: boolean
    email?: boolean
    createdAt?: boolean
  }

  export type UserInclude<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    challenges?: boolean | User$challengesArgs<ExtArgs>
    streaks?: boolean | User$streaksArgs<ExtArgs>
    dailyEntries?: boolean | User$dailyEntriesArgs<ExtArgs>
    waterEntries?: boolean | User$waterEntriesArgs<ExtArgs>
    _count?: boolean | UserCountOutputTypeDefaultArgs<ExtArgs>
  }
  export type UserIncludeCreateManyAndReturn<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {}

  export type $UserPayload<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    name: "User"
    objects: {
      challenges: Prisma.$ChallengePayload<ExtArgs>[]
      streaks: Prisma.$StreakPayload<ExtArgs>[]
      dailyEntries: Prisma.$DailyEntryPayload<ExtArgs>[]
      waterEntries: Prisma.$WaterEntryPayload<ExtArgs>[]
    }
    scalars: $Extensions.GetPayloadResult<{
      id: string
      email: string
      createdAt: Date
    }, ExtArgs["result"]["user"]>
    composites: {}
  }

  type UserGetPayload<S extends boolean | null | undefined | UserDefaultArgs> = $Result.GetResult<Prisma.$UserPayload, S>

  type UserCountArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = 
    Omit<UserFindManyArgs, 'select' | 'include' | 'distinct'> & {
      select?: UserCountAggregateInputType | true
    }

  export interface UserDelegate<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> {
    [K: symbol]: { types: Prisma.TypeMap<ExtArgs>['model']['User'], meta: { name: 'User' } }
    /**
     * Find zero or one User that matches the filter.
     * @param {UserFindUniqueArgs} args - Arguments to find a User
     * @example
     * // Get one User
     * const user = await prisma.user.findUnique({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findUnique<T extends UserFindUniqueArgs>(args: SelectSubset<T, UserFindUniqueArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "findUnique"> | null, null, ExtArgs>

    /**
     * Find one User that matches the filter or throw an error with `error.code='P2025'` 
     * if no matches were found.
     * @param {UserFindUniqueOrThrowArgs} args - Arguments to find a User
     * @example
     * // Get one User
     * const user = await prisma.user.findUniqueOrThrow({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findUniqueOrThrow<T extends UserFindUniqueOrThrowArgs>(args: SelectSubset<T, UserFindUniqueOrThrowArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "findUniqueOrThrow">, never, ExtArgs>

    /**
     * Find the first User that matches the filter.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {UserFindFirstArgs} args - Arguments to find a User
     * @example
     * // Get one User
     * const user = await prisma.user.findFirst({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findFirst<T extends UserFindFirstArgs>(args?: SelectSubset<T, UserFindFirstArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "findFirst"> | null, null, ExtArgs>

    /**
     * Find the first User that matches the filter or
     * throw `PrismaKnownClientError` with `P2025` code if no matches were found.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {UserFindFirstOrThrowArgs} args - Arguments to find a User
     * @example
     * // Get one User
     * const user = await prisma.user.findFirstOrThrow({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findFirstOrThrow<T extends UserFindFirstOrThrowArgs>(args?: SelectSubset<T, UserFindFirstOrThrowArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "findFirstOrThrow">, never, ExtArgs>

    /**
     * Find zero or more Users that matches the filter.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {UserFindManyArgs} args - Arguments to filter and select certain fields only.
     * @example
     * // Get all Users
     * const users = await prisma.user.findMany()
     * 
     * // Get first 10 Users
     * const users = await prisma.user.findMany({ take: 10 })
     * 
     * // Only select the `id`
     * const userWithIdOnly = await prisma.user.findMany({ select: { id: true } })
     * 
     */
    findMany<T extends UserFindManyArgs>(args?: SelectSubset<T, UserFindManyArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "findMany">>

    /**
     * Create a User.
     * @param {UserCreateArgs} args - Arguments to create a User.
     * @example
     * // Create one User
     * const User = await prisma.user.create({
     *   data: {
     *     // ... data to create a User
     *   }
     * })
     * 
     */
    create<T extends UserCreateArgs>(args: SelectSubset<T, UserCreateArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "create">, never, ExtArgs>

    /**
     * Create many Users.
     * @param {UserCreateManyArgs} args - Arguments to create many Users.
     * @example
     * // Create many Users
     * const user = await prisma.user.createMany({
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     *     
     */
    createMany<T extends UserCreateManyArgs>(args?: SelectSubset<T, UserCreateManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Create many Users and returns the data saved in the database.
     * @param {UserCreateManyAndReturnArgs} args - Arguments to create many Users.
     * @example
     * // Create many Users
     * const user = await prisma.user.createManyAndReturn({
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     * 
     * // Create many Users and only return the `id`
     * const userWithIdOnly = await prisma.user.createManyAndReturn({ 
     *   select: { id: true },
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * 
     */
    createManyAndReturn<T extends UserCreateManyAndReturnArgs>(args?: SelectSubset<T, UserCreateManyAndReturnArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "createManyAndReturn">>

    /**
     * Delete a User.
     * @param {UserDeleteArgs} args - Arguments to delete one User.
     * @example
     * // Delete one User
     * const User = await prisma.user.delete({
     *   where: {
     *     // ... filter to delete one User
     *   }
     * })
     * 
     */
    delete<T extends UserDeleteArgs>(args: SelectSubset<T, UserDeleteArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "delete">, never, ExtArgs>

    /**
     * Update one User.
     * @param {UserUpdateArgs} args - Arguments to update one User.
     * @example
     * // Update one User
     * const user = await prisma.user.update({
     *   where: {
     *     // ... provide filter here
     *   },
     *   data: {
     *     // ... provide data here
     *   }
     * })
     * 
     */
    update<T extends UserUpdateArgs>(args: SelectSubset<T, UserUpdateArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "update">, never, ExtArgs>

    /**
     * Delete zero or more Users.
     * @param {UserDeleteManyArgs} args - Arguments to filter Users to delete.
     * @example
     * // Delete a few Users
     * const { count } = await prisma.user.deleteMany({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     * 
     */
    deleteMany<T extends UserDeleteManyArgs>(args?: SelectSubset<T, UserDeleteManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Update zero or more Users.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {UserUpdateManyArgs} args - Arguments to update one or more rows.
     * @example
     * // Update many Users
     * const user = await prisma.user.updateMany({
     *   where: {
     *     // ... provide filter here
     *   },
     *   data: {
     *     // ... provide data here
     *   }
     * })
     * 
     */
    updateMany<T extends UserUpdateManyArgs>(args: SelectSubset<T, UserUpdateManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Create or update one User.
     * @param {UserUpsertArgs} args - Arguments to update or create a User.
     * @example
     * // Update or create a User
     * const user = await prisma.user.upsert({
     *   create: {
     *     // ... data to create a User
     *   },
     *   update: {
     *     // ... in case it already exists, update
     *   },
     *   where: {
     *     // ... the filter for the User we want to update
     *   }
     * })
     */
    upsert<T extends UserUpsertArgs>(args: SelectSubset<T, UserUpsertArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "upsert">, never, ExtArgs>


    /**
     * Count the number of Users.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {UserCountArgs} args - Arguments to filter Users to count.
     * @example
     * // Count the number of Users
     * const count = await prisma.user.count({
     *   where: {
     *     // ... the filter for the Users we want to count
     *   }
     * })
    **/
    count<T extends UserCountArgs>(
      args?: Subset<T, UserCountArgs>,
    ): Prisma.PrismaPromise<
      T extends $Utils.Record<'select', any>
        ? T['select'] extends true
          ? number
          : GetScalarType<T['select'], UserCountAggregateOutputType>
        : number
    >

    /**
     * Allows you to perform aggregations operations on a User.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {UserAggregateArgs} args - Select which aggregations you would like to apply and on what fields.
     * @example
     * // Ordered by age ascending
     * // Where email contains prisma.io
     * // Limited to the 10 users
     * const aggregations = await prisma.user.aggregate({
     *   _avg: {
     *     age: true,
     *   },
     *   where: {
     *     email: {
     *       contains: "prisma.io",
     *     },
     *   },
     *   orderBy: {
     *     age: "asc",
     *   },
     *   take: 10,
     * })
    **/
    aggregate<T extends UserAggregateArgs>(args: Subset<T, UserAggregateArgs>): Prisma.PrismaPromise<GetUserAggregateType<T>>

    /**
     * Group by User.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {UserGroupByArgs} args - Group by arguments.
     * @example
     * // Group by city, order by createdAt, get count
     * const result = await prisma.user.groupBy({
     *   by: ['city', 'createdAt'],
     *   orderBy: {
     *     createdAt: true
     *   },
     *   _count: {
     *     _all: true
     *   },
     * })
     * 
    **/
    groupBy<
      T extends UserGroupByArgs,
      HasSelectOrTake extends Or<
        Extends<'skip', Keys<T>>,
        Extends<'take', Keys<T>>
      >,
      OrderByArg extends True extends HasSelectOrTake
        ? { orderBy: UserGroupByArgs['orderBy'] }
        : { orderBy?: UserGroupByArgs['orderBy'] },
      OrderFields extends ExcludeUnderscoreKeys<Keys<MaybeTupleToUnion<T['orderBy']>>>,
      ByFields extends MaybeTupleToUnion<T['by']>,
      ByValid extends Has<ByFields, OrderFields>,
      HavingFields extends GetHavingFields<T['having']>,
      HavingValid extends Has<ByFields, HavingFields>,
      ByEmpty extends T['by'] extends never[] ? True : False,
      InputErrors extends ByEmpty extends True
      ? `Error: "by" must not be empty.`
      : HavingValid extends False
      ? {
          [P in HavingFields]: P extends ByFields
            ? never
            : P extends string
            ? `Error: Field "${P}" used in "having" needs to be provided in "by".`
            : [
                Error,
                'Field ',
                P,
                ` in "having" needs to be provided in "by"`,
              ]
        }[HavingFields]
      : 'take' extends Keys<T>
      ? 'orderBy' extends Keys<T>
        ? ByValid extends True
          ? {}
          : {
              [P in OrderFields]: P extends ByFields
                ? never
                : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
            }[OrderFields]
        : 'Error: If you provide "take", you also need to provide "orderBy"'
      : 'skip' extends Keys<T>
      ? 'orderBy' extends Keys<T>
        ? ByValid extends True
          ? {}
          : {
              [P in OrderFields]: P extends ByFields
                ? never
                : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
            }[OrderFields]
        : 'Error: If you provide "skip", you also need to provide "orderBy"'
      : ByValid extends True
      ? {}
      : {
          [P in OrderFields]: P extends ByFields
            ? never
            : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
        }[OrderFields]
    >(args: SubsetIntersection<T, UserGroupByArgs, OrderByArg> & InputErrors): {} extends InputErrors ? GetUserGroupByPayload<T> : Prisma.PrismaPromise<InputErrors>
  /**
   * Fields of the User model
   */
  readonly fields: UserFieldRefs;
  }

  /**
   * The delegate class that acts as a "Promise-like" for User.
   * Why is this prefixed with `Prisma__`?
   * Because we want to prevent naming conflicts as mentioned in
   * https://github.com/prisma/prisma-client-js/issues/707
   */
  export interface Prisma__UserClient<T, Null = never, ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> extends Prisma.PrismaPromise<T> {
    readonly [Symbol.toStringTag]: "PrismaPromise"
    challenges<T extends User$challengesArgs<ExtArgs> = {}>(args?: Subset<T, User$challengesArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "findMany"> | Null>
    streaks<T extends User$streaksArgs<ExtArgs> = {}>(args?: Subset<T, User$streaksArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "findMany"> | Null>
    dailyEntries<T extends User$dailyEntriesArgs<ExtArgs> = {}>(args?: Subset<T, User$dailyEntriesArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "findMany"> | Null>
    waterEntries<T extends User$waterEntriesArgs<ExtArgs> = {}>(args?: Subset<T, User$waterEntriesArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "findMany"> | Null>
    /**
     * Attaches callbacks for the resolution and/or rejection of the Promise.
     * @param onfulfilled The callback to execute when the Promise is resolved.
     * @param onrejected The callback to execute when the Promise is rejected.
     * @returns A Promise for the completion of which ever callback is executed.
     */
    then<TResult1 = T, TResult2 = never>(onfulfilled?: ((value: T) => TResult1 | PromiseLike<TResult1>) | undefined | null, onrejected?: ((reason: any) => TResult2 | PromiseLike<TResult2>) | undefined | null): $Utils.JsPromise<TResult1 | TResult2>
    /**
     * Attaches a callback for only the rejection of the Promise.
     * @param onrejected The callback to execute when the Promise is rejected.
     * @returns A Promise for the completion of the callback.
     */
    catch<TResult = never>(onrejected?: ((reason: any) => TResult | PromiseLike<TResult>) | undefined | null): $Utils.JsPromise<T | TResult>
    /**
     * Attaches a callback that is invoked when the Promise is settled (fulfilled or rejected). The
     * resolved value cannot be modified from the callback.
     * @param onfinally The callback to execute when the Promise is settled (fulfilled or rejected).
     * @returns A Promise for the completion of the callback.
     */
    finally(onfinally?: (() => void) | undefined | null): $Utils.JsPromise<T>
  }




  /**
   * Fields of the User model
   */ 
  interface UserFieldRefs {
    readonly id: FieldRef<"User", 'String'>
    readonly email: FieldRef<"User", 'String'>
    readonly createdAt: FieldRef<"User", 'DateTime'>
  }
    

  // Custom InputTypes
  /**
   * User findUnique
   */
  export type UserFindUniqueArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the User
     */
    select?: UserSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: UserInclude<ExtArgs> | null
    /**
     * Filter, which User to fetch.
     */
    where: UserWhereUniqueInput
  }

  /**
   * User findUniqueOrThrow
   */
  export type UserFindUniqueOrThrowArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the User
     */
    select?: UserSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: UserInclude<ExtArgs> | null
    /**
     * Filter, which User to fetch.
     */
    where: UserWhereUniqueInput
  }

  /**
   * User findFirst
   */
  export type UserFindFirstArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the User
     */
    select?: UserSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: UserInclude<ExtArgs> | null
    /**
     * Filter, which User to fetch.
     */
    where?: UserWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Users to fetch.
     */
    orderBy?: UserOrderByWithRelationInput | UserOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for searching for Users.
     */
    cursor?: UserWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Users from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Users.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/distinct Distinct Docs}
     * 
     * Filter by unique combinations of Users.
     */
    distinct?: UserScalarFieldEnum | UserScalarFieldEnum[]
  }

  /**
   * User findFirstOrThrow
   */
  export type UserFindFirstOrThrowArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the User
     */
    select?: UserSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: UserInclude<ExtArgs> | null
    /**
     * Filter, which User to fetch.
     */
    where?: UserWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Users to fetch.
     */
    orderBy?: UserOrderByWithRelationInput | UserOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for searching for Users.
     */
    cursor?: UserWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Users from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Users.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/distinct Distinct Docs}
     * 
     * Filter by unique combinations of Users.
     */
    distinct?: UserScalarFieldEnum | UserScalarFieldEnum[]
  }

  /**
   * User findMany
   */
  export type UserFindManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the User
     */
    select?: UserSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: UserInclude<ExtArgs> | null
    /**
     * Filter, which Users to fetch.
     */
    where?: UserWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Users to fetch.
     */
    orderBy?: UserOrderByWithRelationInput | UserOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for listing Users.
     */
    cursor?: UserWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Users from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Users.
     */
    skip?: number
    distinct?: UserScalarFieldEnum | UserScalarFieldEnum[]
  }

  /**
   * User create
   */
  export type UserCreateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the User
     */
    select?: UserSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: UserInclude<ExtArgs> | null
    /**
     * The data needed to create a User.
     */
    data: XOR<UserCreateInput, UserUncheckedCreateInput>
  }

  /**
   * User createMany
   */
  export type UserCreateManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * The data used to create many Users.
     */
    data: UserCreateManyInput | UserCreateManyInput[]
    skipDuplicates?: boolean
  }

  /**
   * User createManyAndReturn
   */
  export type UserCreateManyAndReturnArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the User
     */
    select?: UserSelectCreateManyAndReturn<ExtArgs> | null
    /**
     * The data used to create many Users.
     */
    data: UserCreateManyInput | UserCreateManyInput[]
    skipDuplicates?: boolean
  }

  /**
   * User update
   */
  export type UserUpdateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the User
     */
    select?: UserSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: UserInclude<ExtArgs> | null
    /**
     * The data needed to update a User.
     */
    data: XOR<UserUpdateInput, UserUncheckedUpdateInput>
    /**
     * Choose, which User to update.
     */
    where: UserWhereUniqueInput
  }

  /**
   * User updateMany
   */
  export type UserUpdateManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * The data used to update Users.
     */
    data: XOR<UserUpdateManyMutationInput, UserUncheckedUpdateManyInput>
    /**
     * Filter which Users to update
     */
    where?: UserWhereInput
  }

  /**
   * User upsert
   */
  export type UserUpsertArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the User
     */
    select?: UserSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: UserInclude<ExtArgs> | null
    /**
     * The filter to search for the User to update in case it exists.
     */
    where: UserWhereUniqueInput
    /**
     * In case the User found by the `where` argument doesn't exist, create a new User with this data.
     */
    create: XOR<UserCreateInput, UserUncheckedCreateInput>
    /**
     * In case the User was found with the provided `where` argument, update it with this data.
     */
    update: XOR<UserUpdateInput, UserUncheckedUpdateInput>
  }

  /**
   * User delete
   */
  export type UserDeleteArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the User
     */
    select?: UserSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: UserInclude<ExtArgs> | null
    /**
     * Filter which User to delete.
     */
    where: UserWhereUniqueInput
  }

  /**
   * User deleteMany
   */
  export type UserDeleteManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Filter which Users to delete
     */
    where?: UserWhereInput
  }

  /**
   * User.challenges
   */
  export type User$challengesArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeInclude<ExtArgs> | null
    where?: ChallengeWhereInput
    orderBy?: ChallengeOrderByWithRelationInput | ChallengeOrderByWithRelationInput[]
    cursor?: ChallengeWhereUniqueInput
    take?: number
    skip?: number
    distinct?: ChallengeScalarFieldEnum | ChallengeScalarFieldEnum[]
  }

  /**
   * User.streaks
   */
  export type User$streaksArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
    where?: StreakWhereInput
    orderBy?: StreakOrderByWithRelationInput | StreakOrderByWithRelationInput[]
    cursor?: StreakWhereUniqueInput
    take?: number
    skip?: number
    distinct?: StreakScalarFieldEnum | StreakScalarFieldEnum[]
  }

  /**
   * User.dailyEntries
   */
  export type User$dailyEntriesArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
    where?: DailyEntryWhereInput
    orderBy?: DailyEntryOrderByWithRelationInput | DailyEntryOrderByWithRelationInput[]
    cursor?: DailyEntryWhereUniqueInput
    take?: number
    skip?: number
    distinct?: DailyEntryScalarFieldEnum | DailyEntryScalarFieldEnum[]
  }

  /**
   * User.waterEntries
   */
  export type User$waterEntriesArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
    where?: WaterEntryWhereInput
    orderBy?: WaterEntryOrderByWithRelationInput | WaterEntryOrderByWithRelationInput[]
    cursor?: WaterEntryWhereUniqueInput
    take?: number
    skip?: number
    distinct?: WaterEntryScalarFieldEnum | WaterEntryScalarFieldEnum[]
  }

  /**
   * User without action
   */
  export type UserDefaultArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the User
     */
    select?: UserSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: UserInclude<ExtArgs> | null
  }


  /**
   * Model Challenge
   */

  export type AggregateChallenge = {
    _count: ChallengeCountAggregateOutputType | null
    _avg: ChallengeAvgAggregateOutputType | null
    _sum: ChallengeSumAggregateOutputType | null
    _min: ChallengeMinAggregateOutputType | null
    _max: ChallengeMaxAggregateOutputType | null
  }

  export type ChallengeAvgAggregateOutputType = {
    duration: number | null
    targetValue: number | null
  }

  export type ChallengeSumAggregateOutputType = {
    duration: number | null
    targetValue: number | null
  }

  export type ChallengeMinAggregateOutputType = {
    id: string | null
    userId: string | null
    name: string | null
    description: string | null
    type: string | null
    category: string | null
    duration: number | null
    startDate: Date | null
    endDate: Date | null
    targetValue: number | null
    unit: string | null
    color: string | null
    icon: string | null
    isActive: boolean | null
    createdAt: Date | null
    updatedAt: Date | null
  }

  export type ChallengeMaxAggregateOutputType = {
    id: string | null
    userId: string | null
    name: string | null
    description: string | null
    type: string | null
    category: string | null
    duration: number | null
    startDate: Date | null
    endDate: Date | null
    targetValue: number | null
    unit: string | null
    color: string | null
    icon: string | null
    isActive: boolean | null
    createdAt: Date | null
    updatedAt: Date | null
  }

  export type ChallengeCountAggregateOutputType = {
    id: number
    userId: number
    name: number
    description: number
    type: number
    category: number
    duration: number
    startDate: number
    endDate: number
    targetValue: number
    unit: number
    color: number
    icon: number
    isActive: number
    createdAt: number
    updatedAt: number
    _all: number
  }


  export type ChallengeAvgAggregateInputType = {
    duration?: true
    targetValue?: true
  }

  export type ChallengeSumAggregateInputType = {
    duration?: true
    targetValue?: true
  }

  export type ChallengeMinAggregateInputType = {
    id?: true
    userId?: true
    name?: true
    description?: true
    type?: true
    category?: true
    duration?: true
    startDate?: true
    endDate?: true
    targetValue?: true
    unit?: true
    color?: true
    icon?: true
    isActive?: true
    createdAt?: true
    updatedAt?: true
  }

  export type ChallengeMaxAggregateInputType = {
    id?: true
    userId?: true
    name?: true
    description?: true
    type?: true
    category?: true
    duration?: true
    startDate?: true
    endDate?: true
    targetValue?: true
    unit?: true
    color?: true
    icon?: true
    isActive?: true
    createdAt?: true
    updatedAt?: true
  }

  export type ChallengeCountAggregateInputType = {
    id?: true
    userId?: true
    name?: true
    description?: true
    type?: true
    category?: true
    duration?: true
    startDate?: true
    endDate?: true
    targetValue?: true
    unit?: true
    color?: true
    icon?: true
    isActive?: true
    createdAt?: true
    updatedAt?: true
    _all?: true
  }

  export type ChallengeAggregateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Filter which Challenge to aggregate.
     */
    where?: ChallengeWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Challenges to fetch.
     */
    orderBy?: ChallengeOrderByWithRelationInput | ChallengeOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the start position
     */
    cursor?: ChallengeWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Challenges from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Challenges.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Count returned Challenges
    **/
    _count?: true | ChallengeCountAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to average
    **/
    _avg?: ChallengeAvgAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to sum
    **/
    _sum?: ChallengeSumAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to find the minimum value
    **/
    _min?: ChallengeMinAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to find the maximum value
    **/
    _max?: ChallengeMaxAggregateInputType
  }

  export type GetChallengeAggregateType<T extends ChallengeAggregateArgs> = {
        [P in keyof T & keyof AggregateChallenge]: P extends '_count' | 'count'
      ? T[P] extends true
        ? number
        : GetScalarType<T[P], AggregateChallenge[P]>
      : GetScalarType<T[P], AggregateChallenge[P]>
  }




  export type ChallengeGroupByArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: ChallengeWhereInput
    orderBy?: ChallengeOrderByWithAggregationInput | ChallengeOrderByWithAggregationInput[]
    by: ChallengeScalarFieldEnum[] | ChallengeScalarFieldEnum
    having?: ChallengeScalarWhereWithAggregatesInput
    take?: number
    skip?: number
    _count?: ChallengeCountAggregateInputType | true
    _avg?: ChallengeAvgAggregateInputType
    _sum?: ChallengeSumAggregateInputType
    _min?: ChallengeMinAggregateInputType
    _max?: ChallengeMaxAggregateInputType
  }

  export type ChallengeGroupByOutputType = {
    id: string
    userId: string
    name: string
    description: string | null
    type: string
    category: string
    duration: number
    startDate: Date
    endDate: Date
    targetValue: number | null
    unit: string | null
    color: string | null
    icon: string | null
    isActive: boolean
    createdAt: Date
    updatedAt: Date
    _count: ChallengeCountAggregateOutputType | null
    _avg: ChallengeAvgAggregateOutputType | null
    _sum: ChallengeSumAggregateOutputType | null
    _min: ChallengeMinAggregateOutputType | null
    _max: ChallengeMaxAggregateOutputType | null
  }

  type GetChallengeGroupByPayload<T extends ChallengeGroupByArgs> = Prisma.PrismaPromise<
    Array<
      PickEnumerable<ChallengeGroupByOutputType, T['by']> &
        {
          [P in ((keyof T) & (keyof ChallengeGroupByOutputType))]: P extends '_count'
            ? T[P] extends boolean
              ? number
              : GetScalarType<T[P], ChallengeGroupByOutputType[P]>
            : GetScalarType<T[P], ChallengeGroupByOutputType[P]>
        }
      >
    >


  export type ChallengeSelect<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = $Extensions.GetSelect<{
    id?: boolean
    userId?: boolean
    name?: boolean
    description?: boolean
    type?: boolean
    category?: boolean
    duration?: boolean
    startDate?: boolean
    endDate?: boolean
    targetValue?: boolean
    unit?: boolean
    color?: boolean
    icon?: boolean
    isActive?: boolean
    createdAt?: boolean
    updatedAt?: boolean
    user?: boolean | UserDefaultArgs<ExtArgs>
    dailyTasks?: boolean | Challenge$dailyTasksArgs<ExtArgs>
    streaks?: boolean | Challenge$streaksArgs<ExtArgs>
    waterEntries?: boolean | Challenge$waterEntriesArgs<ExtArgs>
    _count?: boolean | ChallengeCountOutputTypeDefaultArgs<ExtArgs>
  }, ExtArgs["result"]["challenge"]>

  export type ChallengeSelectCreateManyAndReturn<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = $Extensions.GetSelect<{
    id?: boolean
    userId?: boolean
    name?: boolean
    description?: boolean
    type?: boolean
    category?: boolean
    duration?: boolean
    startDate?: boolean
    endDate?: boolean
    targetValue?: boolean
    unit?: boolean
    color?: boolean
    icon?: boolean
    isActive?: boolean
    createdAt?: boolean
    updatedAt?: boolean
    user?: boolean | UserDefaultArgs<ExtArgs>
  }, ExtArgs["result"]["challenge"]>

  export type ChallengeSelectScalar = {
    id?: boolean
    userId?: boolean
    name?: boolean
    description?: boolean
    type?: boolean
    category?: boolean
    duration?: boolean
    startDate?: boolean
    endDate?: boolean
    targetValue?: boolean
    unit?: boolean
    color?: boolean
    icon?: boolean
    isActive?: boolean
    createdAt?: boolean
    updatedAt?: boolean
  }

  export type ChallengeInclude<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    user?: boolean | UserDefaultArgs<ExtArgs>
    dailyTasks?: boolean | Challenge$dailyTasksArgs<ExtArgs>
    streaks?: boolean | Challenge$streaksArgs<ExtArgs>
    waterEntries?: boolean | Challenge$waterEntriesArgs<ExtArgs>
    _count?: boolean | ChallengeCountOutputTypeDefaultArgs<ExtArgs>
  }
  export type ChallengeIncludeCreateManyAndReturn<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    user?: boolean | UserDefaultArgs<ExtArgs>
  }

  export type $ChallengePayload<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    name: "Challenge"
    objects: {
      user: Prisma.$UserPayload<ExtArgs>
      dailyTasks: Prisma.$DailyEntryPayload<ExtArgs>[]
      streaks: Prisma.$StreakPayload<ExtArgs>[]
      waterEntries: Prisma.$WaterEntryPayload<ExtArgs>[]
    }
    scalars: $Extensions.GetPayloadResult<{
      id: string
      userId: string
      name: string
      description: string | null
      type: string
      category: string
      duration: number
      startDate: Date
      endDate: Date
      targetValue: number | null
      unit: string | null
      color: string | null
      icon: string | null
      isActive: boolean
      createdAt: Date
      updatedAt: Date
    }, ExtArgs["result"]["challenge"]>
    composites: {}
  }

  type ChallengeGetPayload<S extends boolean | null | undefined | ChallengeDefaultArgs> = $Result.GetResult<Prisma.$ChallengePayload, S>

  type ChallengeCountArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = 
    Omit<ChallengeFindManyArgs, 'select' | 'include' | 'distinct'> & {
      select?: ChallengeCountAggregateInputType | true
    }

  export interface ChallengeDelegate<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> {
    [K: symbol]: { types: Prisma.TypeMap<ExtArgs>['model']['Challenge'], meta: { name: 'Challenge' } }
    /**
     * Find zero or one Challenge that matches the filter.
     * @param {ChallengeFindUniqueArgs} args - Arguments to find a Challenge
     * @example
     * // Get one Challenge
     * const challenge = await prisma.challenge.findUnique({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findUnique<T extends ChallengeFindUniqueArgs>(args: SelectSubset<T, ChallengeFindUniqueArgs<ExtArgs>>): Prisma__ChallengeClient<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "findUnique"> | null, null, ExtArgs>

    /**
     * Find one Challenge that matches the filter or throw an error with `error.code='P2025'` 
     * if no matches were found.
     * @param {ChallengeFindUniqueOrThrowArgs} args - Arguments to find a Challenge
     * @example
     * // Get one Challenge
     * const challenge = await prisma.challenge.findUniqueOrThrow({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findUniqueOrThrow<T extends ChallengeFindUniqueOrThrowArgs>(args: SelectSubset<T, ChallengeFindUniqueOrThrowArgs<ExtArgs>>): Prisma__ChallengeClient<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "findUniqueOrThrow">, never, ExtArgs>

    /**
     * Find the first Challenge that matches the filter.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {ChallengeFindFirstArgs} args - Arguments to find a Challenge
     * @example
     * // Get one Challenge
     * const challenge = await prisma.challenge.findFirst({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findFirst<T extends ChallengeFindFirstArgs>(args?: SelectSubset<T, ChallengeFindFirstArgs<ExtArgs>>): Prisma__ChallengeClient<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "findFirst"> | null, null, ExtArgs>

    /**
     * Find the first Challenge that matches the filter or
     * throw `PrismaKnownClientError` with `P2025` code if no matches were found.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {ChallengeFindFirstOrThrowArgs} args - Arguments to find a Challenge
     * @example
     * // Get one Challenge
     * const challenge = await prisma.challenge.findFirstOrThrow({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findFirstOrThrow<T extends ChallengeFindFirstOrThrowArgs>(args?: SelectSubset<T, ChallengeFindFirstOrThrowArgs<ExtArgs>>): Prisma__ChallengeClient<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "findFirstOrThrow">, never, ExtArgs>

    /**
     * Find zero or more Challenges that matches the filter.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {ChallengeFindManyArgs} args - Arguments to filter and select certain fields only.
     * @example
     * // Get all Challenges
     * const challenges = await prisma.challenge.findMany()
     * 
     * // Get first 10 Challenges
     * const challenges = await prisma.challenge.findMany({ take: 10 })
     * 
     * // Only select the `id`
     * const challengeWithIdOnly = await prisma.challenge.findMany({ select: { id: true } })
     * 
     */
    findMany<T extends ChallengeFindManyArgs>(args?: SelectSubset<T, ChallengeFindManyArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "findMany">>

    /**
     * Create a Challenge.
     * @param {ChallengeCreateArgs} args - Arguments to create a Challenge.
     * @example
     * // Create one Challenge
     * const Challenge = await prisma.challenge.create({
     *   data: {
     *     // ... data to create a Challenge
     *   }
     * })
     * 
     */
    create<T extends ChallengeCreateArgs>(args: SelectSubset<T, ChallengeCreateArgs<ExtArgs>>): Prisma__ChallengeClient<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "create">, never, ExtArgs>

    /**
     * Create many Challenges.
     * @param {ChallengeCreateManyArgs} args - Arguments to create many Challenges.
     * @example
     * // Create many Challenges
     * const challenge = await prisma.challenge.createMany({
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     *     
     */
    createMany<T extends ChallengeCreateManyArgs>(args?: SelectSubset<T, ChallengeCreateManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Create many Challenges and returns the data saved in the database.
     * @param {ChallengeCreateManyAndReturnArgs} args - Arguments to create many Challenges.
     * @example
     * // Create many Challenges
     * const challenge = await prisma.challenge.createManyAndReturn({
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     * 
     * // Create many Challenges and only return the `id`
     * const challengeWithIdOnly = await prisma.challenge.createManyAndReturn({ 
     *   select: { id: true },
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * 
     */
    createManyAndReturn<T extends ChallengeCreateManyAndReturnArgs>(args?: SelectSubset<T, ChallengeCreateManyAndReturnArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "createManyAndReturn">>

    /**
     * Delete a Challenge.
     * @param {ChallengeDeleteArgs} args - Arguments to delete one Challenge.
     * @example
     * // Delete one Challenge
     * const Challenge = await prisma.challenge.delete({
     *   where: {
     *     // ... filter to delete one Challenge
     *   }
     * })
     * 
     */
    delete<T extends ChallengeDeleteArgs>(args: SelectSubset<T, ChallengeDeleteArgs<ExtArgs>>): Prisma__ChallengeClient<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "delete">, never, ExtArgs>

    /**
     * Update one Challenge.
     * @param {ChallengeUpdateArgs} args - Arguments to update one Challenge.
     * @example
     * // Update one Challenge
     * const challenge = await prisma.challenge.update({
     *   where: {
     *     // ... provide filter here
     *   },
     *   data: {
     *     // ... provide data here
     *   }
     * })
     * 
     */
    update<T extends ChallengeUpdateArgs>(args: SelectSubset<T, ChallengeUpdateArgs<ExtArgs>>): Prisma__ChallengeClient<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "update">, never, ExtArgs>

    /**
     * Delete zero or more Challenges.
     * @param {ChallengeDeleteManyArgs} args - Arguments to filter Challenges to delete.
     * @example
     * // Delete a few Challenges
     * const { count } = await prisma.challenge.deleteMany({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     * 
     */
    deleteMany<T extends ChallengeDeleteManyArgs>(args?: SelectSubset<T, ChallengeDeleteManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Update zero or more Challenges.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {ChallengeUpdateManyArgs} args - Arguments to update one or more rows.
     * @example
     * // Update many Challenges
     * const challenge = await prisma.challenge.updateMany({
     *   where: {
     *     // ... provide filter here
     *   },
     *   data: {
     *     // ... provide data here
     *   }
     * })
     * 
     */
    updateMany<T extends ChallengeUpdateManyArgs>(args: SelectSubset<T, ChallengeUpdateManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Create or update one Challenge.
     * @param {ChallengeUpsertArgs} args - Arguments to update or create a Challenge.
     * @example
     * // Update or create a Challenge
     * const challenge = await prisma.challenge.upsert({
     *   create: {
     *     // ... data to create a Challenge
     *   },
     *   update: {
     *     // ... in case it already exists, update
     *   },
     *   where: {
     *     // ... the filter for the Challenge we want to update
     *   }
     * })
     */
    upsert<T extends ChallengeUpsertArgs>(args: SelectSubset<T, ChallengeUpsertArgs<ExtArgs>>): Prisma__ChallengeClient<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "upsert">, never, ExtArgs>


    /**
     * Count the number of Challenges.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {ChallengeCountArgs} args - Arguments to filter Challenges to count.
     * @example
     * // Count the number of Challenges
     * const count = await prisma.challenge.count({
     *   where: {
     *     // ... the filter for the Challenges we want to count
     *   }
     * })
    **/
    count<T extends ChallengeCountArgs>(
      args?: Subset<T, ChallengeCountArgs>,
    ): Prisma.PrismaPromise<
      T extends $Utils.Record<'select', any>
        ? T['select'] extends true
          ? number
          : GetScalarType<T['select'], ChallengeCountAggregateOutputType>
        : number
    >

    /**
     * Allows you to perform aggregations operations on a Challenge.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {ChallengeAggregateArgs} args - Select which aggregations you would like to apply and on what fields.
     * @example
     * // Ordered by age ascending
     * // Where email contains prisma.io
     * // Limited to the 10 users
     * const aggregations = await prisma.user.aggregate({
     *   _avg: {
     *     age: true,
     *   },
     *   where: {
     *     email: {
     *       contains: "prisma.io",
     *     },
     *   },
     *   orderBy: {
     *     age: "asc",
     *   },
     *   take: 10,
     * })
    **/
    aggregate<T extends ChallengeAggregateArgs>(args: Subset<T, ChallengeAggregateArgs>): Prisma.PrismaPromise<GetChallengeAggregateType<T>>

    /**
     * Group by Challenge.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {ChallengeGroupByArgs} args - Group by arguments.
     * @example
     * // Group by city, order by createdAt, get count
     * const result = await prisma.user.groupBy({
     *   by: ['city', 'createdAt'],
     *   orderBy: {
     *     createdAt: true
     *   },
     *   _count: {
     *     _all: true
     *   },
     * })
     * 
    **/
    groupBy<
      T extends ChallengeGroupByArgs,
      HasSelectOrTake extends Or<
        Extends<'skip', Keys<T>>,
        Extends<'take', Keys<T>>
      >,
      OrderByArg extends True extends HasSelectOrTake
        ? { orderBy: ChallengeGroupByArgs['orderBy'] }
        : { orderBy?: ChallengeGroupByArgs['orderBy'] },
      OrderFields extends ExcludeUnderscoreKeys<Keys<MaybeTupleToUnion<T['orderBy']>>>,
      ByFields extends MaybeTupleToUnion<T['by']>,
      ByValid extends Has<ByFields, OrderFields>,
      HavingFields extends GetHavingFields<T['having']>,
      HavingValid extends Has<ByFields, HavingFields>,
      ByEmpty extends T['by'] extends never[] ? True : False,
      InputErrors extends ByEmpty extends True
      ? `Error: "by" must not be empty.`
      : HavingValid extends False
      ? {
          [P in HavingFields]: P extends ByFields
            ? never
            : P extends string
            ? `Error: Field "${P}" used in "having" needs to be provided in "by".`
            : [
                Error,
                'Field ',
                P,
                ` in "having" needs to be provided in "by"`,
              ]
        }[HavingFields]
      : 'take' extends Keys<T>
      ? 'orderBy' extends Keys<T>
        ? ByValid extends True
          ? {}
          : {
              [P in OrderFields]: P extends ByFields
                ? never
                : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
            }[OrderFields]
        : 'Error: If you provide "take", you also need to provide "orderBy"'
      : 'skip' extends Keys<T>
      ? 'orderBy' extends Keys<T>
        ? ByValid extends True
          ? {}
          : {
              [P in OrderFields]: P extends ByFields
                ? never
                : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
            }[OrderFields]
        : 'Error: If you provide "skip", you also need to provide "orderBy"'
      : ByValid extends True
      ? {}
      : {
          [P in OrderFields]: P extends ByFields
            ? never
            : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
        }[OrderFields]
    >(args: SubsetIntersection<T, ChallengeGroupByArgs, OrderByArg> & InputErrors): {} extends InputErrors ? GetChallengeGroupByPayload<T> : Prisma.PrismaPromise<InputErrors>
  /**
   * Fields of the Challenge model
   */
  readonly fields: ChallengeFieldRefs;
  }

  /**
   * The delegate class that acts as a "Promise-like" for Challenge.
   * Why is this prefixed with `Prisma__`?
   * Because we want to prevent naming conflicts as mentioned in
   * https://github.com/prisma/prisma-client-js/issues/707
   */
  export interface Prisma__ChallengeClient<T, Null = never, ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> extends Prisma.PrismaPromise<T> {
    readonly [Symbol.toStringTag]: "PrismaPromise"
    user<T extends UserDefaultArgs<ExtArgs> = {}>(args?: Subset<T, UserDefaultArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "findUniqueOrThrow"> | Null, Null, ExtArgs>
    dailyTasks<T extends Challenge$dailyTasksArgs<ExtArgs> = {}>(args?: Subset<T, Challenge$dailyTasksArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "findMany"> | Null>
    streaks<T extends Challenge$streaksArgs<ExtArgs> = {}>(args?: Subset<T, Challenge$streaksArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "findMany"> | Null>
    waterEntries<T extends Challenge$waterEntriesArgs<ExtArgs> = {}>(args?: Subset<T, Challenge$waterEntriesArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "findMany"> | Null>
    /**
     * Attaches callbacks for the resolution and/or rejection of the Promise.
     * @param onfulfilled The callback to execute when the Promise is resolved.
     * @param onrejected The callback to execute when the Promise is rejected.
     * @returns A Promise for the completion of which ever callback is executed.
     */
    then<TResult1 = T, TResult2 = never>(onfulfilled?: ((value: T) => TResult1 | PromiseLike<TResult1>) | undefined | null, onrejected?: ((reason: any) => TResult2 | PromiseLike<TResult2>) | undefined | null): $Utils.JsPromise<TResult1 | TResult2>
    /**
     * Attaches a callback for only the rejection of the Promise.
     * @param onrejected The callback to execute when the Promise is rejected.
     * @returns A Promise for the completion of the callback.
     */
    catch<TResult = never>(onrejected?: ((reason: any) => TResult | PromiseLike<TResult>) | undefined | null): $Utils.JsPromise<T | TResult>
    /**
     * Attaches a callback that is invoked when the Promise is settled (fulfilled or rejected). The
     * resolved value cannot be modified from the callback.
     * @param onfinally The callback to execute when the Promise is settled (fulfilled or rejected).
     * @returns A Promise for the completion of the callback.
     */
    finally(onfinally?: (() => void) | undefined | null): $Utils.JsPromise<T>
  }




  /**
   * Fields of the Challenge model
   */ 
  interface ChallengeFieldRefs {
    readonly id: FieldRef<"Challenge", 'String'>
    readonly userId: FieldRef<"Challenge", 'String'>
    readonly name: FieldRef<"Challenge", 'String'>
    readonly description: FieldRef<"Challenge", 'String'>
    readonly type: FieldRef<"Challenge", 'String'>
    readonly category: FieldRef<"Challenge", 'String'>
    readonly duration: FieldRef<"Challenge", 'Int'>
    readonly startDate: FieldRef<"Challenge", 'DateTime'>
    readonly endDate: FieldRef<"Challenge", 'DateTime'>
    readonly targetValue: FieldRef<"Challenge", 'Float'>
    readonly unit: FieldRef<"Challenge", 'String'>
    readonly color: FieldRef<"Challenge", 'String'>
    readonly icon: FieldRef<"Challenge", 'String'>
    readonly isActive: FieldRef<"Challenge", 'Boolean'>
    readonly createdAt: FieldRef<"Challenge", 'DateTime'>
    readonly updatedAt: FieldRef<"Challenge", 'DateTime'>
  }
    

  // Custom InputTypes
  /**
   * Challenge findUnique
   */
  export type ChallengeFindUniqueArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeInclude<ExtArgs> | null
    /**
     * Filter, which Challenge to fetch.
     */
    where: ChallengeWhereUniqueInput
  }

  /**
   * Challenge findUniqueOrThrow
   */
  export type ChallengeFindUniqueOrThrowArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeInclude<ExtArgs> | null
    /**
     * Filter, which Challenge to fetch.
     */
    where: ChallengeWhereUniqueInput
  }

  /**
   * Challenge findFirst
   */
  export type ChallengeFindFirstArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeInclude<ExtArgs> | null
    /**
     * Filter, which Challenge to fetch.
     */
    where?: ChallengeWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Challenges to fetch.
     */
    orderBy?: ChallengeOrderByWithRelationInput | ChallengeOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for searching for Challenges.
     */
    cursor?: ChallengeWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Challenges from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Challenges.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/distinct Distinct Docs}
     * 
     * Filter by unique combinations of Challenges.
     */
    distinct?: ChallengeScalarFieldEnum | ChallengeScalarFieldEnum[]
  }

  /**
   * Challenge findFirstOrThrow
   */
  export type ChallengeFindFirstOrThrowArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeInclude<ExtArgs> | null
    /**
     * Filter, which Challenge to fetch.
     */
    where?: ChallengeWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Challenges to fetch.
     */
    orderBy?: ChallengeOrderByWithRelationInput | ChallengeOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for searching for Challenges.
     */
    cursor?: ChallengeWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Challenges from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Challenges.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/distinct Distinct Docs}
     * 
     * Filter by unique combinations of Challenges.
     */
    distinct?: ChallengeScalarFieldEnum | ChallengeScalarFieldEnum[]
  }

  /**
   * Challenge findMany
   */
  export type ChallengeFindManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeInclude<ExtArgs> | null
    /**
     * Filter, which Challenges to fetch.
     */
    where?: ChallengeWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Challenges to fetch.
     */
    orderBy?: ChallengeOrderByWithRelationInput | ChallengeOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for listing Challenges.
     */
    cursor?: ChallengeWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Challenges from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Challenges.
     */
    skip?: number
    distinct?: ChallengeScalarFieldEnum | ChallengeScalarFieldEnum[]
  }

  /**
   * Challenge create
   */
  export type ChallengeCreateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeInclude<ExtArgs> | null
    /**
     * The data needed to create a Challenge.
     */
    data: XOR<ChallengeCreateInput, ChallengeUncheckedCreateInput>
  }

  /**
   * Challenge createMany
   */
  export type ChallengeCreateManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * The data used to create many Challenges.
     */
    data: ChallengeCreateManyInput | ChallengeCreateManyInput[]
    skipDuplicates?: boolean
  }

  /**
   * Challenge createManyAndReturn
   */
  export type ChallengeCreateManyAndReturnArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelectCreateManyAndReturn<ExtArgs> | null
    /**
     * The data used to create many Challenges.
     */
    data: ChallengeCreateManyInput | ChallengeCreateManyInput[]
    skipDuplicates?: boolean
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeIncludeCreateManyAndReturn<ExtArgs> | null
  }

  /**
   * Challenge update
   */
  export type ChallengeUpdateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeInclude<ExtArgs> | null
    /**
     * The data needed to update a Challenge.
     */
    data: XOR<ChallengeUpdateInput, ChallengeUncheckedUpdateInput>
    /**
     * Choose, which Challenge to update.
     */
    where: ChallengeWhereUniqueInput
  }

  /**
   * Challenge updateMany
   */
  export type ChallengeUpdateManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * The data used to update Challenges.
     */
    data: XOR<ChallengeUpdateManyMutationInput, ChallengeUncheckedUpdateManyInput>
    /**
     * Filter which Challenges to update
     */
    where?: ChallengeWhereInput
  }

  /**
   * Challenge upsert
   */
  export type ChallengeUpsertArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeInclude<ExtArgs> | null
    /**
     * The filter to search for the Challenge to update in case it exists.
     */
    where: ChallengeWhereUniqueInput
    /**
     * In case the Challenge found by the `where` argument doesn't exist, create a new Challenge with this data.
     */
    create: XOR<ChallengeCreateInput, ChallengeUncheckedCreateInput>
    /**
     * In case the Challenge was found with the provided `where` argument, update it with this data.
     */
    update: XOR<ChallengeUpdateInput, ChallengeUncheckedUpdateInput>
  }

  /**
   * Challenge delete
   */
  export type ChallengeDeleteArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeInclude<ExtArgs> | null
    /**
     * Filter which Challenge to delete.
     */
    where: ChallengeWhereUniqueInput
  }

  /**
   * Challenge deleteMany
   */
  export type ChallengeDeleteManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Filter which Challenges to delete
     */
    where?: ChallengeWhereInput
  }

  /**
   * Challenge.dailyTasks
   */
  export type Challenge$dailyTasksArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
    where?: DailyEntryWhereInput
    orderBy?: DailyEntryOrderByWithRelationInput | DailyEntryOrderByWithRelationInput[]
    cursor?: DailyEntryWhereUniqueInput
    take?: number
    skip?: number
    distinct?: DailyEntryScalarFieldEnum | DailyEntryScalarFieldEnum[]
  }

  /**
   * Challenge.streaks
   */
  export type Challenge$streaksArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
    where?: StreakWhereInput
    orderBy?: StreakOrderByWithRelationInput | StreakOrderByWithRelationInput[]
    cursor?: StreakWhereUniqueInput
    take?: number
    skip?: number
    distinct?: StreakScalarFieldEnum | StreakScalarFieldEnum[]
  }

  /**
   * Challenge.waterEntries
   */
  export type Challenge$waterEntriesArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
    where?: WaterEntryWhereInput
    orderBy?: WaterEntryOrderByWithRelationInput | WaterEntryOrderByWithRelationInput[]
    cursor?: WaterEntryWhereUniqueInput
    take?: number
    skip?: number
    distinct?: WaterEntryScalarFieldEnum | WaterEntryScalarFieldEnum[]
  }

  /**
   * Challenge without action
   */
  export type ChallengeDefaultArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Challenge
     */
    select?: ChallengeSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: ChallengeInclude<ExtArgs> | null
  }


  /**
   * Model WaterEntry
   */

  export type AggregateWaterEntry = {
    _count: WaterEntryCountAggregateOutputType | null
    _avg: WaterEntryAvgAggregateOutputType | null
    _sum: WaterEntrySumAggregateOutputType | null
    _min: WaterEntryMinAggregateOutputType | null
    _max: WaterEntryMaxAggregateOutputType | null
  }

  export type WaterEntryAvgAggregateOutputType = {
    amount: number | null
    targetAmount: number | null
  }

  export type WaterEntrySumAggregateOutputType = {
    amount: number | null
    targetAmount: number | null
  }

  export type WaterEntryMinAggregateOutputType = {
    id: string | null
    userId: string | null
    challengeId: string | null
    date: Date | null
    amount: number | null
    targetAmount: number | null
    completed: boolean | null
    createdAt: Date | null
    updatedAt: Date | null
  }

  export type WaterEntryMaxAggregateOutputType = {
    id: string | null
    userId: string | null
    challengeId: string | null
    date: Date | null
    amount: number | null
    targetAmount: number | null
    completed: boolean | null
    createdAt: Date | null
    updatedAt: Date | null
  }

  export type WaterEntryCountAggregateOutputType = {
    id: number
    userId: number
    challengeId: number
    date: number
    amount: number
    targetAmount: number
    completed: number
    createdAt: number
    updatedAt: number
    _all: number
  }


  export type WaterEntryAvgAggregateInputType = {
    amount?: true
    targetAmount?: true
  }

  export type WaterEntrySumAggregateInputType = {
    amount?: true
    targetAmount?: true
  }

  export type WaterEntryMinAggregateInputType = {
    id?: true
    userId?: true
    challengeId?: true
    date?: true
    amount?: true
    targetAmount?: true
    completed?: true
    createdAt?: true
    updatedAt?: true
  }

  export type WaterEntryMaxAggregateInputType = {
    id?: true
    userId?: true
    challengeId?: true
    date?: true
    amount?: true
    targetAmount?: true
    completed?: true
    createdAt?: true
    updatedAt?: true
  }

  export type WaterEntryCountAggregateInputType = {
    id?: true
    userId?: true
    challengeId?: true
    date?: true
    amount?: true
    targetAmount?: true
    completed?: true
    createdAt?: true
    updatedAt?: true
    _all?: true
  }

  export type WaterEntryAggregateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Filter which WaterEntry to aggregate.
     */
    where?: WaterEntryWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of WaterEntries to fetch.
     */
    orderBy?: WaterEntryOrderByWithRelationInput | WaterEntryOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the start position
     */
    cursor?: WaterEntryWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` WaterEntries from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` WaterEntries.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Count returned WaterEntries
    **/
    _count?: true | WaterEntryCountAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to average
    **/
    _avg?: WaterEntryAvgAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to sum
    **/
    _sum?: WaterEntrySumAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to find the minimum value
    **/
    _min?: WaterEntryMinAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to find the maximum value
    **/
    _max?: WaterEntryMaxAggregateInputType
  }

  export type GetWaterEntryAggregateType<T extends WaterEntryAggregateArgs> = {
        [P in keyof T & keyof AggregateWaterEntry]: P extends '_count' | 'count'
      ? T[P] extends true
        ? number
        : GetScalarType<T[P], AggregateWaterEntry[P]>
      : GetScalarType<T[P], AggregateWaterEntry[P]>
  }




  export type WaterEntryGroupByArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: WaterEntryWhereInput
    orderBy?: WaterEntryOrderByWithAggregationInput | WaterEntryOrderByWithAggregationInput[]
    by: WaterEntryScalarFieldEnum[] | WaterEntryScalarFieldEnum
    having?: WaterEntryScalarWhereWithAggregatesInput
    take?: number
    skip?: number
    _count?: WaterEntryCountAggregateInputType | true
    _avg?: WaterEntryAvgAggregateInputType
    _sum?: WaterEntrySumAggregateInputType
    _min?: WaterEntryMinAggregateInputType
    _max?: WaterEntryMaxAggregateInputType
  }

  export type WaterEntryGroupByOutputType = {
    id: string
    userId: string
    challengeId: string
    date: Date
    amount: number
    targetAmount: number
    completed: boolean
    createdAt: Date
    updatedAt: Date
    _count: WaterEntryCountAggregateOutputType | null
    _avg: WaterEntryAvgAggregateOutputType | null
    _sum: WaterEntrySumAggregateOutputType | null
    _min: WaterEntryMinAggregateOutputType | null
    _max: WaterEntryMaxAggregateOutputType | null
  }

  type GetWaterEntryGroupByPayload<T extends WaterEntryGroupByArgs> = Prisma.PrismaPromise<
    Array<
      PickEnumerable<WaterEntryGroupByOutputType, T['by']> &
        {
          [P in ((keyof T) & (keyof WaterEntryGroupByOutputType))]: P extends '_count'
            ? T[P] extends boolean
              ? number
              : GetScalarType<T[P], WaterEntryGroupByOutputType[P]>
            : GetScalarType<T[P], WaterEntryGroupByOutputType[P]>
        }
      >
    >


  export type WaterEntrySelect<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = $Extensions.GetSelect<{
    id?: boolean
    userId?: boolean
    challengeId?: boolean
    date?: boolean
    amount?: boolean
    targetAmount?: boolean
    completed?: boolean
    createdAt?: boolean
    updatedAt?: boolean
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }, ExtArgs["result"]["waterEntry"]>

  export type WaterEntrySelectCreateManyAndReturn<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = $Extensions.GetSelect<{
    id?: boolean
    userId?: boolean
    challengeId?: boolean
    date?: boolean
    amount?: boolean
    targetAmount?: boolean
    completed?: boolean
    createdAt?: boolean
    updatedAt?: boolean
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }, ExtArgs["result"]["waterEntry"]>

  export type WaterEntrySelectScalar = {
    id?: boolean
    userId?: boolean
    challengeId?: boolean
    date?: boolean
    amount?: boolean
    targetAmount?: boolean
    completed?: boolean
    createdAt?: boolean
    updatedAt?: boolean
  }

  export type WaterEntryInclude<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }
  export type WaterEntryIncludeCreateManyAndReturn<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }

  export type $WaterEntryPayload<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    name: "WaterEntry"
    objects: {
      user: Prisma.$UserPayload<ExtArgs>
      challenge: Prisma.$ChallengePayload<ExtArgs>
    }
    scalars: $Extensions.GetPayloadResult<{
      id: string
      userId: string
      challengeId: string
      date: Date
      amount: number
      targetAmount: number
      completed: boolean
      createdAt: Date
      updatedAt: Date
    }, ExtArgs["result"]["waterEntry"]>
    composites: {}
  }

  type WaterEntryGetPayload<S extends boolean | null | undefined | WaterEntryDefaultArgs> = $Result.GetResult<Prisma.$WaterEntryPayload, S>

  type WaterEntryCountArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = 
    Omit<WaterEntryFindManyArgs, 'select' | 'include' | 'distinct'> & {
      select?: WaterEntryCountAggregateInputType | true
    }

  export interface WaterEntryDelegate<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> {
    [K: symbol]: { types: Prisma.TypeMap<ExtArgs>['model']['WaterEntry'], meta: { name: 'WaterEntry' } }
    /**
     * Find zero or one WaterEntry that matches the filter.
     * @param {WaterEntryFindUniqueArgs} args - Arguments to find a WaterEntry
     * @example
     * // Get one WaterEntry
     * const waterEntry = await prisma.waterEntry.findUnique({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findUnique<T extends WaterEntryFindUniqueArgs>(args: SelectSubset<T, WaterEntryFindUniqueArgs<ExtArgs>>): Prisma__WaterEntryClient<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "findUnique"> | null, null, ExtArgs>

    /**
     * Find one WaterEntry that matches the filter or throw an error with `error.code='P2025'` 
     * if no matches were found.
     * @param {WaterEntryFindUniqueOrThrowArgs} args - Arguments to find a WaterEntry
     * @example
     * // Get one WaterEntry
     * const waterEntry = await prisma.waterEntry.findUniqueOrThrow({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findUniqueOrThrow<T extends WaterEntryFindUniqueOrThrowArgs>(args: SelectSubset<T, WaterEntryFindUniqueOrThrowArgs<ExtArgs>>): Prisma__WaterEntryClient<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "findUniqueOrThrow">, never, ExtArgs>

    /**
     * Find the first WaterEntry that matches the filter.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {WaterEntryFindFirstArgs} args - Arguments to find a WaterEntry
     * @example
     * // Get one WaterEntry
     * const waterEntry = await prisma.waterEntry.findFirst({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findFirst<T extends WaterEntryFindFirstArgs>(args?: SelectSubset<T, WaterEntryFindFirstArgs<ExtArgs>>): Prisma__WaterEntryClient<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "findFirst"> | null, null, ExtArgs>

    /**
     * Find the first WaterEntry that matches the filter or
     * throw `PrismaKnownClientError` with `P2025` code if no matches were found.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {WaterEntryFindFirstOrThrowArgs} args - Arguments to find a WaterEntry
     * @example
     * // Get one WaterEntry
     * const waterEntry = await prisma.waterEntry.findFirstOrThrow({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findFirstOrThrow<T extends WaterEntryFindFirstOrThrowArgs>(args?: SelectSubset<T, WaterEntryFindFirstOrThrowArgs<ExtArgs>>): Prisma__WaterEntryClient<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "findFirstOrThrow">, never, ExtArgs>

    /**
     * Find zero or more WaterEntries that matches the filter.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {WaterEntryFindManyArgs} args - Arguments to filter and select certain fields only.
     * @example
     * // Get all WaterEntries
     * const waterEntries = await prisma.waterEntry.findMany()
     * 
     * // Get first 10 WaterEntries
     * const waterEntries = await prisma.waterEntry.findMany({ take: 10 })
     * 
     * // Only select the `id`
     * const waterEntryWithIdOnly = await prisma.waterEntry.findMany({ select: { id: true } })
     * 
     */
    findMany<T extends WaterEntryFindManyArgs>(args?: SelectSubset<T, WaterEntryFindManyArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "findMany">>

    /**
     * Create a WaterEntry.
     * @param {WaterEntryCreateArgs} args - Arguments to create a WaterEntry.
     * @example
     * // Create one WaterEntry
     * const WaterEntry = await prisma.waterEntry.create({
     *   data: {
     *     // ... data to create a WaterEntry
     *   }
     * })
     * 
     */
    create<T extends WaterEntryCreateArgs>(args: SelectSubset<T, WaterEntryCreateArgs<ExtArgs>>): Prisma__WaterEntryClient<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "create">, never, ExtArgs>

    /**
     * Create many WaterEntries.
     * @param {WaterEntryCreateManyArgs} args - Arguments to create many WaterEntries.
     * @example
     * // Create many WaterEntries
     * const waterEntry = await prisma.waterEntry.createMany({
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     *     
     */
    createMany<T extends WaterEntryCreateManyArgs>(args?: SelectSubset<T, WaterEntryCreateManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Create many WaterEntries and returns the data saved in the database.
     * @param {WaterEntryCreateManyAndReturnArgs} args - Arguments to create many WaterEntries.
     * @example
     * // Create many WaterEntries
     * const waterEntry = await prisma.waterEntry.createManyAndReturn({
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     * 
     * // Create many WaterEntries and only return the `id`
     * const waterEntryWithIdOnly = await prisma.waterEntry.createManyAndReturn({ 
     *   select: { id: true },
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * 
     */
    createManyAndReturn<T extends WaterEntryCreateManyAndReturnArgs>(args?: SelectSubset<T, WaterEntryCreateManyAndReturnArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "createManyAndReturn">>

    /**
     * Delete a WaterEntry.
     * @param {WaterEntryDeleteArgs} args - Arguments to delete one WaterEntry.
     * @example
     * // Delete one WaterEntry
     * const WaterEntry = await prisma.waterEntry.delete({
     *   where: {
     *     // ... filter to delete one WaterEntry
     *   }
     * })
     * 
     */
    delete<T extends WaterEntryDeleteArgs>(args: SelectSubset<T, WaterEntryDeleteArgs<ExtArgs>>): Prisma__WaterEntryClient<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "delete">, never, ExtArgs>

    /**
     * Update one WaterEntry.
     * @param {WaterEntryUpdateArgs} args - Arguments to update one WaterEntry.
     * @example
     * // Update one WaterEntry
     * const waterEntry = await prisma.waterEntry.update({
     *   where: {
     *     // ... provide filter here
     *   },
     *   data: {
     *     // ... provide data here
     *   }
     * })
     * 
     */
    update<T extends WaterEntryUpdateArgs>(args: SelectSubset<T, WaterEntryUpdateArgs<ExtArgs>>): Prisma__WaterEntryClient<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "update">, never, ExtArgs>

    /**
     * Delete zero or more WaterEntries.
     * @param {WaterEntryDeleteManyArgs} args - Arguments to filter WaterEntries to delete.
     * @example
     * // Delete a few WaterEntries
     * const { count } = await prisma.waterEntry.deleteMany({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     * 
     */
    deleteMany<T extends WaterEntryDeleteManyArgs>(args?: SelectSubset<T, WaterEntryDeleteManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Update zero or more WaterEntries.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {WaterEntryUpdateManyArgs} args - Arguments to update one or more rows.
     * @example
     * // Update many WaterEntries
     * const waterEntry = await prisma.waterEntry.updateMany({
     *   where: {
     *     // ... provide filter here
     *   },
     *   data: {
     *     // ... provide data here
     *   }
     * })
     * 
     */
    updateMany<T extends WaterEntryUpdateManyArgs>(args: SelectSubset<T, WaterEntryUpdateManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Create or update one WaterEntry.
     * @param {WaterEntryUpsertArgs} args - Arguments to update or create a WaterEntry.
     * @example
     * // Update or create a WaterEntry
     * const waterEntry = await prisma.waterEntry.upsert({
     *   create: {
     *     // ... data to create a WaterEntry
     *   },
     *   update: {
     *     // ... in case it already exists, update
     *   },
     *   where: {
     *     // ... the filter for the WaterEntry we want to update
     *   }
     * })
     */
    upsert<T extends WaterEntryUpsertArgs>(args: SelectSubset<T, WaterEntryUpsertArgs<ExtArgs>>): Prisma__WaterEntryClient<$Result.GetResult<Prisma.$WaterEntryPayload<ExtArgs>, T, "upsert">, never, ExtArgs>


    /**
     * Count the number of WaterEntries.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {WaterEntryCountArgs} args - Arguments to filter WaterEntries to count.
     * @example
     * // Count the number of WaterEntries
     * const count = await prisma.waterEntry.count({
     *   where: {
     *     // ... the filter for the WaterEntries we want to count
     *   }
     * })
    **/
    count<T extends WaterEntryCountArgs>(
      args?: Subset<T, WaterEntryCountArgs>,
    ): Prisma.PrismaPromise<
      T extends $Utils.Record<'select', any>
        ? T['select'] extends true
          ? number
          : GetScalarType<T['select'], WaterEntryCountAggregateOutputType>
        : number
    >

    /**
     * Allows you to perform aggregations operations on a WaterEntry.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {WaterEntryAggregateArgs} args - Select which aggregations you would like to apply and on what fields.
     * @example
     * // Ordered by age ascending
     * // Where email contains prisma.io
     * // Limited to the 10 users
     * const aggregations = await prisma.user.aggregate({
     *   _avg: {
     *     age: true,
     *   },
     *   where: {
     *     email: {
     *       contains: "prisma.io",
     *     },
     *   },
     *   orderBy: {
     *     age: "asc",
     *   },
     *   take: 10,
     * })
    **/
    aggregate<T extends WaterEntryAggregateArgs>(args: Subset<T, WaterEntryAggregateArgs>): Prisma.PrismaPromise<GetWaterEntryAggregateType<T>>

    /**
     * Group by WaterEntry.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {WaterEntryGroupByArgs} args - Group by arguments.
     * @example
     * // Group by city, order by createdAt, get count
     * const result = await prisma.user.groupBy({
     *   by: ['city', 'createdAt'],
     *   orderBy: {
     *     createdAt: true
     *   },
     *   _count: {
     *     _all: true
     *   },
     * })
     * 
    **/
    groupBy<
      T extends WaterEntryGroupByArgs,
      HasSelectOrTake extends Or<
        Extends<'skip', Keys<T>>,
        Extends<'take', Keys<T>>
      >,
      OrderByArg extends True extends HasSelectOrTake
        ? { orderBy: WaterEntryGroupByArgs['orderBy'] }
        : { orderBy?: WaterEntryGroupByArgs['orderBy'] },
      OrderFields extends ExcludeUnderscoreKeys<Keys<MaybeTupleToUnion<T['orderBy']>>>,
      ByFields extends MaybeTupleToUnion<T['by']>,
      ByValid extends Has<ByFields, OrderFields>,
      HavingFields extends GetHavingFields<T['having']>,
      HavingValid extends Has<ByFields, HavingFields>,
      ByEmpty extends T['by'] extends never[] ? True : False,
      InputErrors extends ByEmpty extends True
      ? `Error: "by" must not be empty.`
      : HavingValid extends False
      ? {
          [P in HavingFields]: P extends ByFields
            ? never
            : P extends string
            ? `Error: Field "${P}" used in "having" needs to be provided in "by".`
            : [
                Error,
                'Field ',
                P,
                ` in "having" needs to be provided in "by"`,
              ]
        }[HavingFields]
      : 'take' extends Keys<T>
      ? 'orderBy' extends Keys<T>
        ? ByValid extends True
          ? {}
          : {
              [P in OrderFields]: P extends ByFields
                ? never
                : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
            }[OrderFields]
        : 'Error: If you provide "take", you also need to provide "orderBy"'
      : 'skip' extends Keys<T>
      ? 'orderBy' extends Keys<T>
        ? ByValid extends True
          ? {}
          : {
              [P in OrderFields]: P extends ByFields
                ? never
                : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
            }[OrderFields]
        : 'Error: If you provide "skip", you also need to provide "orderBy"'
      : ByValid extends True
      ? {}
      : {
          [P in OrderFields]: P extends ByFields
            ? never
            : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
        }[OrderFields]
    >(args: SubsetIntersection<T, WaterEntryGroupByArgs, OrderByArg> & InputErrors): {} extends InputErrors ? GetWaterEntryGroupByPayload<T> : Prisma.PrismaPromise<InputErrors>
  /**
   * Fields of the WaterEntry model
   */
  readonly fields: WaterEntryFieldRefs;
  }

  /**
   * The delegate class that acts as a "Promise-like" for WaterEntry.
   * Why is this prefixed with `Prisma__`?
   * Because we want to prevent naming conflicts as mentioned in
   * https://github.com/prisma/prisma-client-js/issues/707
   */
  export interface Prisma__WaterEntryClient<T, Null = never, ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> extends Prisma.PrismaPromise<T> {
    readonly [Symbol.toStringTag]: "PrismaPromise"
    user<T extends UserDefaultArgs<ExtArgs> = {}>(args?: Subset<T, UserDefaultArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "findUniqueOrThrow"> | Null, Null, ExtArgs>
    challenge<T extends ChallengeDefaultArgs<ExtArgs> = {}>(args?: Subset<T, ChallengeDefaultArgs<ExtArgs>>): Prisma__ChallengeClient<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "findUniqueOrThrow"> | Null, Null, ExtArgs>
    /**
     * Attaches callbacks for the resolution and/or rejection of the Promise.
     * @param onfulfilled The callback to execute when the Promise is resolved.
     * @param onrejected The callback to execute when the Promise is rejected.
     * @returns A Promise for the completion of which ever callback is executed.
     */
    then<TResult1 = T, TResult2 = never>(onfulfilled?: ((value: T) => TResult1 | PromiseLike<TResult1>) | undefined | null, onrejected?: ((reason: any) => TResult2 | PromiseLike<TResult2>) | undefined | null): $Utils.JsPromise<TResult1 | TResult2>
    /**
     * Attaches a callback for only the rejection of the Promise.
     * @param onrejected The callback to execute when the Promise is rejected.
     * @returns A Promise for the completion of the callback.
     */
    catch<TResult = never>(onrejected?: ((reason: any) => TResult | PromiseLike<TResult>) | undefined | null): $Utils.JsPromise<T | TResult>
    /**
     * Attaches a callback that is invoked when the Promise is settled (fulfilled or rejected). The
     * resolved value cannot be modified from the callback.
     * @param onfinally The callback to execute when the Promise is settled (fulfilled or rejected).
     * @returns A Promise for the completion of the callback.
     */
    finally(onfinally?: (() => void) | undefined | null): $Utils.JsPromise<T>
  }




  /**
   * Fields of the WaterEntry model
   */ 
  interface WaterEntryFieldRefs {
    readonly id: FieldRef<"WaterEntry", 'String'>
    readonly userId: FieldRef<"WaterEntry", 'String'>
    readonly challengeId: FieldRef<"WaterEntry", 'String'>
    readonly date: FieldRef<"WaterEntry", 'DateTime'>
    readonly amount: FieldRef<"WaterEntry", 'Float'>
    readonly targetAmount: FieldRef<"WaterEntry", 'Float'>
    readonly completed: FieldRef<"WaterEntry", 'Boolean'>
    readonly createdAt: FieldRef<"WaterEntry", 'DateTime'>
    readonly updatedAt: FieldRef<"WaterEntry", 'DateTime'>
  }
    

  // Custom InputTypes
  /**
   * WaterEntry findUnique
   */
  export type WaterEntryFindUniqueArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
    /**
     * Filter, which WaterEntry to fetch.
     */
    where: WaterEntryWhereUniqueInput
  }

  /**
   * WaterEntry findUniqueOrThrow
   */
  export type WaterEntryFindUniqueOrThrowArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
    /**
     * Filter, which WaterEntry to fetch.
     */
    where: WaterEntryWhereUniqueInput
  }

  /**
   * WaterEntry findFirst
   */
  export type WaterEntryFindFirstArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
    /**
     * Filter, which WaterEntry to fetch.
     */
    where?: WaterEntryWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of WaterEntries to fetch.
     */
    orderBy?: WaterEntryOrderByWithRelationInput | WaterEntryOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for searching for WaterEntries.
     */
    cursor?: WaterEntryWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` WaterEntries from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` WaterEntries.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/distinct Distinct Docs}
     * 
     * Filter by unique combinations of WaterEntries.
     */
    distinct?: WaterEntryScalarFieldEnum | WaterEntryScalarFieldEnum[]
  }

  /**
   * WaterEntry findFirstOrThrow
   */
  export type WaterEntryFindFirstOrThrowArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
    /**
     * Filter, which WaterEntry to fetch.
     */
    where?: WaterEntryWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of WaterEntries to fetch.
     */
    orderBy?: WaterEntryOrderByWithRelationInput | WaterEntryOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for searching for WaterEntries.
     */
    cursor?: WaterEntryWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` WaterEntries from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` WaterEntries.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/distinct Distinct Docs}
     * 
     * Filter by unique combinations of WaterEntries.
     */
    distinct?: WaterEntryScalarFieldEnum | WaterEntryScalarFieldEnum[]
  }

  /**
   * WaterEntry findMany
   */
  export type WaterEntryFindManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
    /**
     * Filter, which WaterEntries to fetch.
     */
    where?: WaterEntryWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of WaterEntries to fetch.
     */
    orderBy?: WaterEntryOrderByWithRelationInput | WaterEntryOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for listing WaterEntries.
     */
    cursor?: WaterEntryWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` WaterEntries from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` WaterEntries.
     */
    skip?: number
    distinct?: WaterEntryScalarFieldEnum | WaterEntryScalarFieldEnum[]
  }

  /**
   * WaterEntry create
   */
  export type WaterEntryCreateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
    /**
     * The data needed to create a WaterEntry.
     */
    data: XOR<WaterEntryCreateInput, WaterEntryUncheckedCreateInput>
  }

  /**
   * WaterEntry createMany
   */
  export type WaterEntryCreateManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * The data used to create many WaterEntries.
     */
    data: WaterEntryCreateManyInput | WaterEntryCreateManyInput[]
    skipDuplicates?: boolean
  }

  /**
   * WaterEntry createManyAndReturn
   */
  export type WaterEntryCreateManyAndReturnArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelectCreateManyAndReturn<ExtArgs> | null
    /**
     * The data used to create many WaterEntries.
     */
    data: WaterEntryCreateManyInput | WaterEntryCreateManyInput[]
    skipDuplicates?: boolean
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryIncludeCreateManyAndReturn<ExtArgs> | null
  }

  /**
   * WaterEntry update
   */
  export type WaterEntryUpdateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
    /**
     * The data needed to update a WaterEntry.
     */
    data: XOR<WaterEntryUpdateInput, WaterEntryUncheckedUpdateInput>
    /**
     * Choose, which WaterEntry to update.
     */
    where: WaterEntryWhereUniqueInput
  }

  /**
   * WaterEntry updateMany
   */
  export type WaterEntryUpdateManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * The data used to update WaterEntries.
     */
    data: XOR<WaterEntryUpdateManyMutationInput, WaterEntryUncheckedUpdateManyInput>
    /**
     * Filter which WaterEntries to update
     */
    where?: WaterEntryWhereInput
  }

  /**
   * WaterEntry upsert
   */
  export type WaterEntryUpsertArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
    /**
     * The filter to search for the WaterEntry to update in case it exists.
     */
    where: WaterEntryWhereUniqueInput
    /**
     * In case the WaterEntry found by the `where` argument doesn't exist, create a new WaterEntry with this data.
     */
    create: XOR<WaterEntryCreateInput, WaterEntryUncheckedCreateInput>
    /**
     * In case the WaterEntry was found with the provided `where` argument, update it with this data.
     */
    update: XOR<WaterEntryUpdateInput, WaterEntryUncheckedUpdateInput>
  }

  /**
   * WaterEntry delete
   */
  export type WaterEntryDeleteArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
    /**
     * Filter which WaterEntry to delete.
     */
    where: WaterEntryWhereUniqueInput
  }

  /**
   * WaterEntry deleteMany
   */
  export type WaterEntryDeleteManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Filter which WaterEntries to delete
     */
    where?: WaterEntryWhereInput
  }

  /**
   * WaterEntry without action
   */
  export type WaterEntryDefaultArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the WaterEntry
     */
    select?: WaterEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: WaterEntryInclude<ExtArgs> | null
  }


  /**
   * Model DailyEntry
   */

  export type AggregateDailyEntry = {
    _count: DailyEntryCountAggregateOutputType | null
    _avg: DailyEntryAvgAggregateOutputType | null
    _sum: DailyEntrySumAggregateOutputType | null
    _min: DailyEntryMinAggregateOutputType | null
    _max: DailyEntryMaxAggregateOutputType | null
  }

  export type DailyEntryAvgAggregateOutputType = {
    dayNumber: number | null
    value: number | null
  }

  export type DailyEntrySumAggregateOutputType = {
    dayNumber: number | null
    value: number | null
  }

  export type DailyEntryMinAggregateOutputType = {
    id: string | null
    userId: string | null
    challengeId: string | null
    dayNumber: number | null
    date: Date | null
    completed: boolean | null
    completedAt: Date | null
    notes: string | null
    value: number | null
  }

  export type DailyEntryMaxAggregateOutputType = {
    id: string | null
    userId: string | null
    challengeId: string | null
    dayNumber: number | null
    date: Date | null
    completed: boolean | null
    completedAt: Date | null
    notes: string | null
    value: number | null
  }

  export type DailyEntryCountAggregateOutputType = {
    id: number
    userId: number
    challengeId: number
    dayNumber: number
    date: number
    completed: number
    completedAt: number
    notes: number
    value: number
    _all: number
  }


  export type DailyEntryAvgAggregateInputType = {
    dayNumber?: true
    value?: true
  }

  export type DailyEntrySumAggregateInputType = {
    dayNumber?: true
    value?: true
  }

  export type DailyEntryMinAggregateInputType = {
    id?: true
    userId?: true
    challengeId?: true
    dayNumber?: true
    date?: true
    completed?: true
    completedAt?: true
    notes?: true
    value?: true
  }

  export type DailyEntryMaxAggregateInputType = {
    id?: true
    userId?: true
    challengeId?: true
    dayNumber?: true
    date?: true
    completed?: true
    completedAt?: true
    notes?: true
    value?: true
  }

  export type DailyEntryCountAggregateInputType = {
    id?: true
    userId?: true
    challengeId?: true
    dayNumber?: true
    date?: true
    completed?: true
    completedAt?: true
    notes?: true
    value?: true
    _all?: true
  }

  export type DailyEntryAggregateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Filter which DailyEntry to aggregate.
     */
    where?: DailyEntryWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of DailyEntries to fetch.
     */
    orderBy?: DailyEntryOrderByWithRelationInput | DailyEntryOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the start position
     */
    cursor?: DailyEntryWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` DailyEntries from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` DailyEntries.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Count returned DailyEntries
    **/
    _count?: true | DailyEntryCountAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to average
    **/
    _avg?: DailyEntryAvgAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to sum
    **/
    _sum?: DailyEntrySumAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to find the minimum value
    **/
    _min?: DailyEntryMinAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to find the maximum value
    **/
    _max?: DailyEntryMaxAggregateInputType
  }

  export type GetDailyEntryAggregateType<T extends DailyEntryAggregateArgs> = {
        [P in keyof T & keyof AggregateDailyEntry]: P extends '_count' | 'count'
      ? T[P] extends true
        ? number
        : GetScalarType<T[P], AggregateDailyEntry[P]>
      : GetScalarType<T[P], AggregateDailyEntry[P]>
  }




  export type DailyEntryGroupByArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: DailyEntryWhereInput
    orderBy?: DailyEntryOrderByWithAggregationInput | DailyEntryOrderByWithAggregationInput[]
    by: DailyEntryScalarFieldEnum[] | DailyEntryScalarFieldEnum
    having?: DailyEntryScalarWhereWithAggregatesInput
    take?: number
    skip?: number
    _count?: DailyEntryCountAggregateInputType | true
    _avg?: DailyEntryAvgAggregateInputType
    _sum?: DailyEntrySumAggregateInputType
    _min?: DailyEntryMinAggregateInputType
    _max?: DailyEntryMaxAggregateInputType
  }

  export type DailyEntryGroupByOutputType = {
    id: string
    userId: string
    challengeId: string
    dayNumber: number
    date: Date
    completed: boolean
    completedAt: Date | null
    notes: string | null
    value: number | null
    _count: DailyEntryCountAggregateOutputType | null
    _avg: DailyEntryAvgAggregateOutputType | null
    _sum: DailyEntrySumAggregateOutputType | null
    _min: DailyEntryMinAggregateOutputType | null
    _max: DailyEntryMaxAggregateOutputType | null
  }

  type GetDailyEntryGroupByPayload<T extends DailyEntryGroupByArgs> = Prisma.PrismaPromise<
    Array<
      PickEnumerable<DailyEntryGroupByOutputType, T['by']> &
        {
          [P in ((keyof T) & (keyof DailyEntryGroupByOutputType))]: P extends '_count'
            ? T[P] extends boolean
              ? number
              : GetScalarType<T[P], DailyEntryGroupByOutputType[P]>
            : GetScalarType<T[P], DailyEntryGroupByOutputType[P]>
        }
      >
    >


  export type DailyEntrySelect<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = $Extensions.GetSelect<{
    id?: boolean
    userId?: boolean
    challengeId?: boolean
    dayNumber?: boolean
    date?: boolean
    completed?: boolean
    completedAt?: boolean
    notes?: boolean
    value?: boolean
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }, ExtArgs["result"]["dailyEntry"]>

  export type DailyEntrySelectCreateManyAndReturn<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = $Extensions.GetSelect<{
    id?: boolean
    userId?: boolean
    challengeId?: boolean
    dayNumber?: boolean
    date?: boolean
    completed?: boolean
    completedAt?: boolean
    notes?: boolean
    value?: boolean
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }, ExtArgs["result"]["dailyEntry"]>

  export type DailyEntrySelectScalar = {
    id?: boolean
    userId?: boolean
    challengeId?: boolean
    dayNumber?: boolean
    date?: boolean
    completed?: boolean
    completedAt?: boolean
    notes?: boolean
    value?: boolean
  }

  export type DailyEntryInclude<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }
  export type DailyEntryIncludeCreateManyAndReturn<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }

  export type $DailyEntryPayload<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    name: "DailyEntry"
    objects: {
      user: Prisma.$UserPayload<ExtArgs>
      challenge: Prisma.$ChallengePayload<ExtArgs>
    }
    scalars: $Extensions.GetPayloadResult<{
      id: string
      userId: string
      challengeId: string
      dayNumber: number
      date: Date
      completed: boolean
      completedAt: Date | null
      notes: string | null
      value: number | null
    }, ExtArgs["result"]["dailyEntry"]>
    composites: {}
  }

  type DailyEntryGetPayload<S extends boolean | null | undefined | DailyEntryDefaultArgs> = $Result.GetResult<Prisma.$DailyEntryPayload, S>

  type DailyEntryCountArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = 
    Omit<DailyEntryFindManyArgs, 'select' | 'include' | 'distinct'> & {
      select?: DailyEntryCountAggregateInputType | true
    }

  export interface DailyEntryDelegate<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> {
    [K: symbol]: { types: Prisma.TypeMap<ExtArgs>['model']['DailyEntry'], meta: { name: 'DailyEntry' } }
    /**
     * Find zero or one DailyEntry that matches the filter.
     * @param {DailyEntryFindUniqueArgs} args - Arguments to find a DailyEntry
     * @example
     * // Get one DailyEntry
     * const dailyEntry = await prisma.dailyEntry.findUnique({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findUnique<T extends DailyEntryFindUniqueArgs>(args: SelectSubset<T, DailyEntryFindUniqueArgs<ExtArgs>>): Prisma__DailyEntryClient<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "findUnique"> | null, null, ExtArgs>

    /**
     * Find one DailyEntry that matches the filter or throw an error with `error.code='P2025'` 
     * if no matches were found.
     * @param {DailyEntryFindUniqueOrThrowArgs} args - Arguments to find a DailyEntry
     * @example
     * // Get one DailyEntry
     * const dailyEntry = await prisma.dailyEntry.findUniqueOrThrow({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findUniqueOrThrow<T extends DailyEntryFindUniqueOrThrowArgs>(args: SelectSubset<T, DailyEntryFindUniqueOrThrowArgs<ExtArgs>>): Prisma__DailyEntryClient<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "findUniqueOrThrow">, never, ExtArgs>

    /**
     * Find the first DailyEntry that matches the filter.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {DailyEntryFindFirstArgs} args - Arguments to find a DailyEntry
     * @example
     * // Get one DailyEntry
     * const dailyEntry = await prisma.dailyEntry.findFirst({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findFirst<T extends DailyEntryFindFirstArgs>(args?: SelectSubset<T, DailyEntryFindFirstArgs<ExtArgs>>): Prisma__DailyEntryClient<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "findFirst"> | null, null, ExtArgs>

    /**
     * Find the first DailyEntry that matches the filter or
     * throw `PrismaKnownClientError` with `P2025` code if no matches were found.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {DailyEntryFindFirstOrThrowArgs} args - Arguments to find a DailyEntry
     * @example
     * // Get one DailyEntry
     * const dailyEntry = await prisma.dailyEntry.findFirstOrThrow({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findFirstOrThrow<T extends DailyEntryFindFirstOrThrowArgs>(args?: SelectSubset<T, DailyEntryFindFirstOrThrowArgs<ExtArgs>>): Prisma__DailyEntryClient<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "findFirstOrThrow">, never, ExtArgs>

    /**
     * Find zero or more DailyEntries that matches the filter.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {DailyEntryFindManyArgs} args - Arguments to filter and select certain fields only.
     * @example
     * // Get all DailyEntries
     * const dailyEntries = await prisma.dailyEntry.findMany()
     * 
     * // Get first 10 DailyEntries
     * const dailyEntries = await prisma.dailyEntry.findMany({ take: 10 })
     * 
     * // Only select the `id`
     * const dailyEntryWithIdOnly = await prisma.dailyEntry.findMany({ select: { id: true } })
     * 
     */
    findMany<T extends DailyEntryFindManyArgs>(args?: SelectSubset<T, DailyEntryFindManyArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "findMany">>

    /**
     * Create a DailyEntry.
     * @param {DailyEntryCreateArgs} args - Arguments to create a DailyEntry.
     * @example
     * // Create one DailyEntry
     * const DailyEntry = await prisma.dailyEntry.create({
     *   data: {
     *     // ... data to create a DailyEntry
     *   }
     * })
     * 
     */
    create<T extends DailyEntryCreateArgs>(args: SelectSubset<T, DailyEntryCreateArgs<ExtArgs>>): Prisma__DailyEntryClient<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "create">, never, ExtArgs>

    /**
     * Create many DailyEntries.
     * @param {DailyEntryCreateManyArgs} args - Arguments to create many DailyEntries.
     * @example
     * // Create many DailyEntries
     * const dailyEntry = await prisma.dailyEntry.createMany({
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     *     
     */
    createMany<T extends DailyEntryCreateManyArgs>(args?: SelectSubset<T, DailyEntryCreateManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Create many DailyEntries and returns the data saved in the database.
     * @param {DailyEntryCreateManyAndReturnArgs} args - Arguments to create many DailyEntries.
     * @example
     * // Create many DailyEntries
     * const dailyEntry = await prisma.dailyEntry.createManyAndReturn({
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     * 
     * // Create many DailyEntries and only return the `id`
     * const dailyEntryWithIdOnly = await prisma.dailyEntry.createManyAndReturn({ 
     *   select: { id: true },
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * 
     */
    createManyAndReturn<T extends DailyEntryCreateManyAndReturnArgs>(args?: SelectSubset<T, DailyEntryCreateManyAndReturnArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "createManyAndReturn">>

    /**
     * Delete a DailyEntry.
     * @param {DailyEntryDeleteArgs} args - Arguments to delete one DailyEntry.
     * @example
     * // Delete one DailyEntry
     * const DailyEntry = await prisma.dailyEntry.delete({
     *   where: {
     *     // ... filter to delete one DailyEntry
     *   }
     * })
     * 
     */
    delete<T extends DailyEntryDeleteArgs>(args: SelectSubset<T, DailyEntryDeleteArgs<ExtArgs>>): Prisma__DailyEntryClient<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "delete">, never, ExtArgs>

    /**
     * Update one DailyEntry.
     * @param {DailyEntryUpdateArgs} args - Arguments to update one DailyEntry.
     * @example
     * // Update one DailyEntry
     * const dailyEntry = await prisma.dailyEntry.update({
     *   where: {
     *     // ... provide filter here
     *   },
     *   data: {
     *     // ... provide data here
     *   }
     * })
     * 
     */
    update<T extends DailyEntryUpdateArgs>(args: SelectSubset<T, DailyEntryUpdateArgs<ExtArgs>>): Prisma__DailyEntryClient<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "update">, never, ExtArgs>

    /**
     * Delete zero or more DailyEntries.
     * @param {DailyEntryDeleteManyArgs} args - Arguments to filter DailyEntries to delete.
     * @example
     * // Delete a few DailyEntries
     * const { count } = await prisma.dailyEntry.deleteMany({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     * 
     */
    deleteMany<T extends DailyEntryDeleteManyArgs>(args?: SelectSubset<T, DailyEntryDeleteManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Update zero or more DailyEntries.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {DailyEntryUpdateManyArgs} args - Arguments to update one or more rows.
     * @example
     * // Update many DailyEntries
     * const dailyEntry = await prisma.dailyEntry.updateMany({
     *   where: {
     *     // ... provide filter here
     *   },
     *   data: {
     *     // ... provide data here
     *   }
     * })
     * 
     */
    updateMany<T extends DailyEntryUpdateManyArgs>(args: SelectSubset<T, DailyEntryUpdateManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Create or update one DailyEntry.
     * @param {DailyEntryUpsertArgs} args - Arguments to update or create a DailyEntry.
     * @example
     * // Update or create a DailyEntry
     * const dailyEntry = await prisma.dailyEntry.upsert({
     *   create: {
     *     // ... data to create a DailyEntry
     *   },
     *   update: {
     *     // ... in case it already exists, update
     *   },
     *   where: {
     *     // ... the filter for the DailyEntry we want to update
     *   }
     * })
     */
    upsert<T extends DailyEntryUpsertArgs>(args: SelectSubset<T, DailyEntryUpsertArgs<ExtArgs>>): Prisma__DailyEntryClient<$Result.GetResult<Prisma.$DailyEntryPayload<ExtArgs>, T, "upsert">, never, ExtArgs>


    /**
     * Count the number of DailyEntries.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {DailyEntryCountArgs} args - Arguments to filter DailyEntries to count.
     * @example
     * // Count the number of DailyEntries
     * const count = await prisma.dailyEntry.count({
     *   where: {
     *     // ... the filter for the DailyEntries we want to count
     *   }
     * })
    **/
    count<T extends DailyEntryCountArgs>(
      args?: Subset<T, DailyEntryCountArgs>,
    ): Prisma.PrismaPromise<
      T extends $Utils.Record<'select', any>
        ? T['select'] extends true
          ? number
          : GetScalarType<T['select'], DailyEntryCountAggregateOutputType>
        : number
    >

    /**
     * Allows you to perform aggregations operations on a DailyEntry.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {DailyEntryAggregateArgs} args - Select which aggregations you would like to apply and on what fields.
     * @example
     * // Ordered by age ascending
     * // Where email contains prisma.io
     * // Limited to the 10 users
     * const aggregations = await prisma.user.aggregate({
     *   _avg: {
     *     age: true,
     *   },
     *   where: {
     *     email: {
     *       contains: "prisma.io",
     *     },
     *   },
     *   orderBy: {
     *     age: "asc",
     *   },
     *   take: 10,
     * })
    **/
    aggregate<T extends DailyEntryAggregateArgs>(args: Subset<T, DailyEntryAggregateArgs>): Prisma.PrismaPromise<GetDailyEntryAggregateType<T>>

    /**
     * Group by DailyEntry.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {DailyEntryGroupByArgs} args - Group by arguments.
     * @example
     * // Group by city, order by createdAt, get count
     * const result = await prisma.user.groupBy({
     *   by: ['city', 'createdAt'],
     *   orderBy: {
     *     createdAt: true
     *   },
     *   _count: {
     *     _all: true
     *   },
     * })
     * 
    **/
    groupBy<
      T extends DailyEntryGroupByArgs,
      HasSelectOrTake extends Or<
        Extends<'skip', Keys<T>>,
        Extends<'take', Keys<T>>
      >,
      OrderByArg extends True extends HasSelectOrTake
        ? { orderBy: DailyEntryGroupByArgs['orderBy'] }
        : { orderBy?: DailyEntryGroupByArgs['orderBy'] },
      OrderFields extends ExcludeUnderscoreKeys<Keys<MaybeTupleToUnion<T['orderBy']>>>,
      ByFields extends MaybeTupleToUnion<T['by']>,
      ByValid extends Has<ByFields, OrderFields>,
      HavingFields extends GetHavingFields<T['having']>,
      HavingValid extends Has<ByFields, HavingFields>,
      ByEmpty extends T['by'] extends never[] ? True : False,
      InputErrors extends ByEmpty extends True
      ? `Error: "by" must not be empty.`
      : HavingValid extends False
      ? {
          [P in HavingFields]: P extends ByFields
            ? never
            : P extends string
            ? `Error: Field "${P}" used in "having" needs to be provided in "by".`
            : [
                Error,
                'Field ',
                P,
                ` in "having" needs to be provided in "by"`,
              ]
        }[HavingFields]
      : 'take' extends Keys<T>
      ? 'orderBy' extends Keys<T>
        ? ByValid extends True
          ? {}
          : {
              [P in OrderFields]: P extends ByFields
                ? never
                : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
            }[OrderFields]
        : 'Error: If you provide "take", you also need to provide "orderBy"'
      : 'skip' extends Keys<T>
      ? 'orderBy' extends Keys<T>
        ? ByValid extends True
          ? {}
          : {
              [P in OrderFields]: P extends ByFields
                ? never
                : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
            }[OrderFields]
        : 'Error: If you provide "skip", you also need to provide "orderBy"'
      : ByValid extends True
      ? {}
      : {
          [P in OrderFields]: P extends ByFields
            ? never
            : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
        }[OrderFields]
    >(args: SubsetIntersection<T, DailyEntryGroupByArgs, OrderByArg> & InputErrors): {} extends InputErrors ? GetDailyEntryGroupByPayload<T> : Prisma.PrismaPromise<InputErrors>
  /**
   * Fields of the DailyEntry model
   */
  readonly fields: DailyEntryFieldRefs;
  }

  /**
   * The delegate class that acts as a "Promise-like" for DailyEntry.
   * Why is this prefixed with `Prisma__`?
   * Because we want to prevent naming conflicts as mentioned in
   * https://github.com/prisma/prisma-client-js/issues/707
   */
  export interface Prisma__DailyEntryClient<T, Null = never, ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> extends Prisma.PrismaPromise<T> {
    readonly [Symbol.toStringTag]: "PrismaPromise"
    user<T extends UserDefaultArgs<ExtArgs> = {}>(args?: Subset<T, UserDefaultArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "findUniqueOrThrow"> | Null, Null, ExtArgs>
    challenge<T extends ChallengeDefaultArgs<ExtArgs> = {}>(args?: Subset<T, ChallengeDefaultArgs<ExtArgs>>): Prisma__ChallengeClient<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "findUniqueOrThrow"> | Null, Null, ExtArgs>
    /**
     * Attaches callbacks for the resolution and/or rejection of the Promise.
     * @param onfulfilled The callback to execute when the Promise is resolved.
     * @param onrejected The callback to execute when the Promise is rejected.
     * @returns A Promise for the completion of which ever callback is executed.
     */
    then<TResult1 = T, TResult2 = never>(onfulfilled?: ((value: T) => TResult1 | PromiseLike<TResult1>) | undefined | null, onrejected?: ((reason: any) => TResult2 | PromiseLike<TResult2>) | undefined | null): $Utils.JsPromise<TResult1 | TResult2>
    /**
     * Attaches a callback for only the rejection of the Promise.
     * @param onrejected The callback to execute when the Promise is rejected.
     * @returns A Promise for the completion of the callback.
     */
    catch<TResult = never>(onrejected?: ((reason: any) => TResult | PromiseLike<TResult>) | undefined | null): $Utils.JsPromise<T | TResult>
    /**
     * Attaches a callback that is invoked when the Promise is settled (fulfilled or rejected). The
     * resolved value cannot be modified from the callback.
     * @param onfinally The callback to execute when the Promise is settled (fulfilled or rejected).
     * @returns A Promise for the completion of the callback.
     */
    finally(onfinally?: (() => void) | undefined | null): $Utils.JsPromise<T>
  }




  /**
   * Fields of the DailyEntry model
   */ 
  interface DailyEntryFieldRefs {
    readonly id: FieldRef<"DailyEntry", 'String'>
    readonly userId: FieldRef<"DailyEntry", 'String'>
    readonly challengeId: FieldRef<"DailyEntry", 'String'>
    readonly dayNumber: FieldRef<"DailyEntry", 'Int'>
    readonly date: FieldRef<"DailyEntry", 'DateTime'>
    readonly completed: FieldRef<"DailyEntry", 'Boolean'>
    readonly completedAt: FieldRef<"DailyEntry", 'DateTime'>
    readonly notes: FieldRef<"DailyEntry", 'String'>
    readonly value: FieldRef<"DailyEntry", 'Float'>
  }
    

  // Custom InputTypes
  /**
   * DailyEntry findUnique
   */
  export type DailyEntryFindUniqueArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
    /**
     * Filter, which DailyEntry to fetch.
     */
    where: DailyEntryWhereUniqueInput
  }

  /**
   * DailyEntry findUniqueOrThrow
   */
  export type DailyEntryFindUniqueOrThrowArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
    /**
     * Filter, which DailyEntry to fetch.
     */
    where: DailyEntryWhereUniqueInput
  }

  /**
   * DailyEntry findFirst
   */
  export type DailyEntryFindFirstArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
    /**
     * Filter, which DailyEntry to fetch.
     */
    where?: DailyEntryWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of DailyEntries to fetch.
     */
    orderBy?: DailyEntryOrderByWithRelationInput | DailyEntryOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for searching for DailyEntries.
     */
    cursor?: DailyEntryWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` DailyEntries from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` DailyEntries.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/distinct Distinct Docs}
     * 
     * Filter by unique combinations of DailyEntries.
     */
    distinct?: DailyEntryScalarFieldEnum | DailyEntryScalarFieldEnum[]
  }

  /**
   * DailyEntry findFirstOrThrow
   */
  export type DailyEntryFindFirstOrThrowArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
    /**
     * Filter, which DailyEntry to fetch.
     */
    where?: DailyEntryWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of DailyEntries to fetch.
     */
    orderBy?: DailyEntryOrderByWithRelationInput | DailyEntryOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for searching for DailyEntries.
     */
    cursor?: DailyEntryWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` DailyEntries from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` DailyEntries.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/distinct Distinct Docs}
     * 
     * Filter by unique combinations of DailyEntries.
     */
    distinct?: DailyEntryScalarFieldEnum | DailyEntryScalarFieldEnum[]
  }

  /**
   * DailyEntry findMany
   */
  export type DailyEntryFindManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
    /**
     * Filter, which DailyEntries to fetch.
     */
    where?: DailyEntryWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of DailyEntries to fetch.
     */
    orderBy?: DailyEntryOrderByWithRelationInput | DailyEntryOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for listing DailyEntries.
     */
    cursor?: DailyEntryWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` DailyEntries from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` DailyEntries.
     */
    skip?: number
    distinct?: DailyEntryScalarFieldEnum | DailyEntryScalarFieldEnum[]
  }

  /**
   * DailyEntry create
   */
  export type DailyEntryCreateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
    /**
     * The data needed to create a DailyEntry.
     */
    data: XOR<DailyEntryCreateInput, DailyEntryUncheckedCreateInput>
  }

  /**
   * DailyEntry createMany
   */
  export type DailyEntryCreateManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * The data used to create many DailyEntries.
     */
    data: DailyEntryCreateManyInput | DailyEntryCreateManyInput[]
    skipDuplicates?: boolean
  }

  /**
   * DailyEntry createManyAndReturn
   */
  export type DailyEntryCreateManyAndReturnArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelectCreateManyAndReturn<ExtArgs> | null
    /**
     * The data used to create many DailyEntries.
     */
    data: DailyEntryCreateManyInput | DailyEntryCreateManyInput[]
    skipDuplicates?: boolean
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryIncludeCreateManyAndReturn<ExtArgs> | null
  }

  /**
   * DailyEntry update
   */
  export type DailyEntryUpdateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
    /**
     * The data needed to update a DailyEntry.
     */
    data: XOR<DailyEntryUpdateInput, DailyEntryUncheckedUpdateInput>
    /**
     * Choose, which DailyEntry to update.
     */
    where: DailyEntryWhereUniqueInput
  }

  /**
   * DailyEntry updateMany
   */
  export type DailyEntryUpdateManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * The data used to update DailyEntries.
     */
    data: XOR<DailyEntryUpdateManyMutationInput, DailyEntryUncheckedUpdateManyInput>
    /**
     * Filter which DailyEntries to update
     */
    where?: DailyEntryWhereInput
  }

  /**
   * DailyEntry upsert
   */
  export type DailyEntryUpsertArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
    /**
     * The filter to search for the DailyEntry to update in case it exists.
     */
    where: DailyEntryWhereUniqueInput
    /**
     * In case the DailyEntry found by the `where` argument doesn't exist, create a new DailyEntry with this data.
     */
    create: XOR<DailyEntryCreateInput, DailyEntryUncheckedCreateInput>
    /**
     * In case the DailyEntry was found with the provided `where` argument, update it with this data.
     */
    update: XOR<DailyEntryUpdateInput, DailyEntryUncheckedUpdateInput>
  }

  /**
   * DailyEntry delete
   */
  export type DailyEntryDeleteArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
    /**
     * Filter which DailyEntry to delete.
     */
    where: DailyEntryWhereUniqueInput
  }

  /**
   * DailyEntry deleteMany
   */
  export type DailyEntryDeleteManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Filter which DailyEntries to delete
     */
    where?: DailyEntryWhereInput
  }

  /**
   * DailyEntry without action
   */
  export type DailyEntryDefaultArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the DailyEntry
     */
    select?: DailyEntrySelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: DailyEntryInclude<ExtArgs> | null
  }


  /**
   * Model Streak
   */

  export type AggregateStreak = {
    _count: StreakCountAggregateOutputType | null
    _avg: StreakAvgAggregateOutputType | null
    _sum: StreakSumAggregateOutputType | null
    _min: StreakMinAggregateOutputType | null
    _max: StreakMaxAggregateOutputType | null
  }

  export type StreakAvgAggregateOutputType = {
    length: number | null
    missedDays: number | null
    freezesUsed: number | null
    lastCompletedDay: number | null
  }

  export type StreakSumAggregateOutputType = {
    length: number | null
    missedDays: number | null
    freezesUsed: number | null
    lastCompletedDay: number | null
  }

  export type StreakMinAggregateOutputType = {
    id: string | null
    userId: string | null
    challengeId: string | null
    length: number | null
    missedDays: number | null
    freezesUsed: number | null
    lastUpdated: Date | null
    isActive: boolean | null
    lastCompletedDay: number | null
  }

  export type StreakMaxAggregateOutputType = {
    id: string | null
    userId: string | null
    challengeId: string | null
    length: number | null
    missedDays: number | null
    freezesUsed: number | null
    lastUpdated: Date | null
    isActive: boolean | null
    lastCompletedDay: number | null
  }

  export type StreakCountAggregateOutputType = {
    id: number
    userId: number
    challengeId: number
    length: number
    missedDays: number
    freezesUsed: number
    lastUpdated: number
    isActive: number
    lastCompletedDay: number
    _all: number
  }


  export type StreakAvgAggregateInputType = {
    length?: true
    missedDays?: true
    freezesUsed?: true
    lastCompletedDay?: true
  }

  export type StreakSumAggregateInputType = {
    length?: true
    missedDays?: true
    freezesUsed?: true
    lastCompletedDay?: true
  }

  export type StreakMinAggregateInputType = {
    id?: true
    userId?: true
    challengeId?: true
    length?: true
    missedDays?: true
    freezesUsed?: true
    lastUpdated?: true
    isActive?: true
    lastCompletedDay?: true
  }

  export type StreakMaxAggregateInputType = {
    id?: true
    userId?: true
    challengeId?: true
    length?: true
    missedDays?: true
    freezesUsed?: true
    lastUpdated?: true
    isActive?: true
    lastCompletedDay?: true
  }

  export type StreakCountAggregateInputType = {
    id?: true
    userId?: true
    challengeId?: true
    length?: true
    missedDays?: true
    freezesUsed?: true
    lastUpdated?: true
    isActive?: true
    lastCompletedDay?: true
    _all?: true
  }

  export type StreakAggregateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Filter which Streak to aggregate.
     */
    where?: StreakWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Streaks to fetch.
     */
    orderBy?: StreakOrderByWithRelationInput | StreakOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the start position
     */
    cursor?: StreakWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Streaks from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Streaks.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Count returned Streaks
    **/
    _count?: true | StreakCountAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to average
    **/
    _avg?: StreakAvgAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to sum
    **/
    _sum?: StreakSumAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to find the minimum value
    **/
    _min?: StreakMinAggregateInputType
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/aggregations Aggregation Docs}
     * 
     * Select which fields to find the maximum value
    **/
    _max?: StreakMaxAggregateInputType
  }

  export type GetStreakAggregateType<T extends StreakAggregateArgs> = {
        [P in keyof T & keyof AggregateStreak]: P extends '_count' | 'count'
      ? T[P] extends true
        ? number
        : GetScalarType<T[P], AggregateStreak[P]>
      : GetScalarType<T[P], AggregateStreak[P]>
  }




  export type StreakGroupByArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    where?: StreakWhereInput
    orderBy?: StreakOrderByWithAggregationInput | StreakOrderByWithAggregationInput[]
    by: StreakScalarFieldEnum[] | StreakScalarFieldEnum
    having?: StreakScalarWhereWithAggregatesInput
    take?: number
    skip?: number
    _count?: StreakCountAggregateInputType | true
    _avg?: StreakAvgAggregateInputType
    _sum?: StreakSumAggregateInputType
    _min?: StreakMinAggregateInputType
    _max?: StreakMaxAggregateInputType
  }

  export type StreakGroupByOutputType = {
    id: string
    userId: string
    challengeId: string
    length: number
    missedDays: number
    freezesUsed: number
    lastUpdated: Date
    isActive: boolean
    lastCompletedDay: number
    _count: StreakCountAggregateOutputType | null
    _avg: StreakAvgAggregateOutputType | null
    _sum: StreakSumAggregateOutputType | null
    _min: StreakMinAggregateOutputType | null
    _max: StreakMaxAggregateOutputType | null
  }

  type GetStreakGroupByPayload<T extends StreakGroupByArgs> = Prisma.PrismaPromise<
    Array<
      PickEnumerable<StreakGroupByOutputType, T['by']> &
        {
          [P in ((keyof T) & (keyof StreakGroupByOutputType))]: P extends '_count'
            ? T[P] extends boolean
              ? number
              : GetScalarType<T[P], StreakGroupByOutputType[P]>
            : GetScalarType<T[P], StreakGroupByOutputType[P]>
        }
      >
    >


  export type StreakSelect<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = $Extensions.GetSelect<{
    id?: boolean
    userId?: boolean
    challengeId?: boolean
    length?: boolean
    missedDays?: boolean
    freezesUsed?: boolean
    lastUpdated?: boolean
    isActive?: boolean
    lastCompletedDay?: boolean
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }, ExtArgs["result"]["streak"]>

  export type StreakSelectCreateManyAndReturn<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = $Extensions.GetSelect<{
    id?: boolean
    userId?: boolean
    challengeId?: boolean
    length?: boolean
    missedDays?: boolean
    freezesUsed?: boolean
    lastUpdated?: boolean
    isActive?: boolean
    lastCompletedDay?: boolean
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }, ExtArgs["result"]["streak"]>

  export type StreakSelectScalar = {
    id?: boolean
    userId?: boolean
    challengeId?: boolean
    length?: boolean
    missedDays?: boolean
    freezesUsed?: boolean
    lastUpdated?: boolean
    isActive?: boolean
    lastCompletedDay?: boolean
  }

  export type StreakInclude<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }
  export type StreakIncludeCreateManyAndReturn<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    user?: boolean | UserDefaultArgs<ExtArgs>
    challenge?: boolean | ChallengeDefaultArgs<ExtArgs>
  }

  export type $StreakPayload<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    name: "Streak"
    objects: {
      user: Prisma.$UserPayload<ExtArgs>
      challenge: Prisma.$ChallengePayload<ExtArgs>
    }
    scalars: $Extensions.GetPayloadResult<{
      id: string
      userId: string
      challengeId: string
      length: number
      missedDays: number
      freezesUsed: number
      lastUpdated: Date
      isActive: boolean
      lastCompletedDay: number
    }, ExtArgs["result"]["streak"]>
    composites: {}
  }

  type StreakGetPayload<S extends boolean | null | undefined | StreakDefaultArgs> = $Result.GetResult<Prisma.$StreakPayload, S>

  type StreakCountArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = 
    Omit<StreakFindManyArgs, 'select' | 'include' | 'distinct'> & {
      select?: StreakCountAggregateInputType | true
    }

  export interface StreakDelegate<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> {
    [K: symbol]: { types: Prisma.TypeMap<ExtArgs>['model']['Streak'], meta: { name: 'Streak' } }
    /**
     * Find zero or one Streak that matches the filter.
     * @param {StreakFindUniqueArgs} args - Arguments to find a Streak
     * @example
     * // Get one Streak
     * const streak = await prisma.streak.findUnique({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findUnique<T extends StreakFindUniqueArgs>(args: SelectSubset<T, StreakFindUniqueArgs<ExtArgs>>): Prisma__StreakClient<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "findUnique"> | null, null, ExtArgs>

    /**
     * Find one Streak that matches the filter or throw an error with `error.code='P2025'` 
     * if no matches were found.
     * @param {StreakFindUniqueOrThrowArgs} args - Arguments to find a Streak
     * @example
     * // Get one Streak
     * const streak = await prisma.streak.findUniqueOrThrow({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findUniqueOrThrow<T extends StreakFindUniqueOrThrowArgs>(args: SelectSubset<T, StreakFindUniqueOrThrowArgs<ExtArgs>>): Prisma__StreakClient<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "findUniqueOrThrow">, never, ExtArgs>

    /**
     * Find the first Streak that matches the filter.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {StreakFindFirstArgs} args - Arguments to find a Streak
     * @example
     * // Get one Streak
     * const streak = await prisma.streak.findFirst({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findFirst<T extends StreakFindFirstArgs>(args?: SelectSubset<T, StreakFindFirstArgs<ExtArgs>>): Prisma__StreakClient<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "findFirst"> | null, null, ExtArgs>

    /**
     * Find the first Streak that matches the filter or
     * throw `PrismaKnownClientError` with `P2025` code if no matches were found.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {StreakFindFirstOrThrowArgs} args - Arguments to find a Streak
     * @example
     * // Get one Streak
     * const streak = await prisma.streak.findFirstOrThrow({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     */
    findFirstOrThrow<T extends StreakFindFirstOrThrowArgs>(args?: SelectSubset<T, StreakFindFirstOrThrowArgs<ExtArgs>>): Prisma__StreakClient<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "findFirstOrThrow">, never, ExtArgs>

    /**
     * Find zero or more Streaks that matches the filter.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {StreakFindManyArgs} args - Arguments to filter and select certain fields only.
     * @example
     * // Get all Streaks
     * const streaks = await prisma.streak.findMany()
     * 
     * // Get first 10 Streaks
     * const streaks = await prisma.streak.findMany({ take: 10 })
     * 
     * // Only select the `id`
     * const streakWithIdOnly = await prisma.streak.findMany({ select: { id: true } })
     * 
     */
    findMany<T extends StreakFindManyArgs>(args?: SelectSubset<T, StreakFindManyArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "findMany">>

    /**
     * Create a Streak.
     * @param {StreakCreateArgs} args - Arguments to create a Streak.
     * @example
     * // Create one Streak
     * const Streak = await prisma.streak.create({
     *   data: {
     *     // ... data to create a Streak
     *   }
     * })
     * 
     */
    create<T extends StreakCreateArgs>(args: SelectSubset<T, StreakCreateArgs<ExtArgs>>): Prisma__StreakClient<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "create">, never, ExtArgs>

    /**
     * Create many Streaks.
     * @param {StreakCreateManyArgs} args - Arguments to create many Streaks.
     * @example
     * // Create many Streaks
     * const streak = await prisma.streak.createMany({
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     *     
     */
    createMany<T extends StreakCreateManyArgs>(args?: SelectSubset<T, StreakCreateManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Create many Streaks and returns the data saved in the database.
     * @param {StreakCreateManyAndReturnArgs} args - Arguments to create many Streaks.
     * @example
     * // Create many Streaks
     * const streak = await prisma.streak.createManyAndReturn({
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     * 
     * // Create many Streaks and only return the `id`
     * const streakWithIdOnly = await prisma.streak.createManyAndReturn({ 
     *   select: { id: true },
     *   data: [
     *     // ... provide data here
     *   ]
     * })
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * 
     */
    createManyAndReturn<T extends StreakCreateManyAndReturnArgs>(args?: SelectSubset<T, StreakCreateManyAndReturnArgs<ExtArgs>>): Prisma.PrismaPromise<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "createManyAndReturn">>

    /**
     * Delete a Streak.
     * @param {StreakDeleteArgs} args - Arguments to delete one Streak.
     * @example
     * // Delete one Streak
     * const Streak = await prisma.streak.delete({
     *   where: {
     *     // ... filter to delete one Streak
     *   }
     * })
     * 
     */
    delete<T extends StreakDeleteArgs>(args: SelectSubset<T, StreakDeleteArgs<ExtArgs>>): Prisma__StreakClient<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "delete">, never, ExtArgs>

    /**
     * Update one Streak.
     * @param {StreakUpdateArgs} args - Arguments to update one Streak.
     * @example
     * // Update one Streak
     * const streak = await prisma.streak.update({
     *   where: {
     *     // ... provide filter here
     *   },
     *   data: {
     *     // ... provide data here
     *   }
     * })
     * 
     */
    update<T extends StreakUpdateArgs>(args: SelectSubset<T, StreakUpdateArgs<ExtArgs>>): Prisma__StreakClient<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "update">, never, ExtArgs>

    /**
     * Delete zero or more Streaks.
     * @param {StreakDeleteManyArgs} args - Arguments to filter Streaks to delete.
     * @example
     * // Delete a few Streaks
     * const { count } = await prisma.streak.deleteMany({
     *   where: {
     *     // ... provide filter here
     *   }
     * })
     * 
     */
    deleteMany<T extends StreakDeleteManyArgs>(args?: SelectSubset<T, StreakDeleteManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Update zero or more Streaks.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {StreakUpdateManyArgs} args - Arguments to update one or more rows.
     * @example
     * // Update many Streaks
     * const streak = await prisma.streak.updateMany({
     *   where: {
     *     // ... provide filter here
     *   },
     *   data: {
     *     // ... provide data here
     *   }
     * })
     * 
     */
    updateMany<T extends StreakUpdateManyArgs>(args: SelectSubset<T, StreakUpdateManyArgs<ExtArgs>>): Prisma.PrismaPromise<BatchPayload>

    /**
     * Create or update one Streak.
     * @param {StreakUpsertArgs} args - Arguments to update or create a Streak.
     * @example
     * // Update or create a Streak
     * const streak = await prisma.streak.upsert({
     *   create: {
     *     // ... data to create a Streak
     *   },
     *   update: {
     *     // ... in case it already exists, update
     *   },
     *   where: {
     *     // ... the filter for the Streak we want to update
     *   }
     * })
     */
    upsert<T extends StreakUpsertArgs>(args: SelectSubset<T, StreakUpsertArgs<ExtArgs>>): Prisma__StreakClient<$Result.GetResult<Prisma.$StreakPayload<ExtArgs>, T, "upsert">, never, ExtArgs>


    /**
     * Count the number of Streaks.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {StreakCountArgs} args - Arguments to filter Streaks to count.
     * @example
     * // Count the number of Streaks
     * const count = await prisma.streak.count({
     *   where: {
     *     // ... the filter for the Streaks we want to count
     *   }
     * })
    **/
    count<T extends StreakCountArgs>(
      args?: Subset<T, StreakCountArgs>,
    ): Prisma.PrismaPromise<
      T extends $Utils.Record<'select', any>
        ? T['select'] extends true
          ? number
          : GetScalarType<T['select'], StreakCountAggregateOutputType>
        : number
    >

    /**
     * Allows you to perform aggregations operations on a Streak.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {StreakAggregateArgs} args - Select which aggregations you would like to apply and on what fields.
     * @example
     * // Ordered by age ascending
     * // Where email contains prisma.io
     * // Limited to the 10 users
     * const aggregations = await prisma.user.aggregate({
     *   _avg: {
     *     age: true,
     *   },
     *   where: {
     *     email: {
     *       contains: "prisma.io",
     *     },
     *   },
     *   orderBy: {
     *     age: "asc",
     *   },
     *   take: 10,
     * })
    **/
    aggregate<T extends StreakAggregateArgs>(args: Subset<T, StreakAggregateArgs>): Prisma.PrismaPromise<GetStreakAggregateType<T>>

    /**
     * Group by Streak.
     * Note, that providing `undefined` is treated as the value not being there.
     * Read more here: https://pris.ly/d/null-undefined
     * @param {StreakGroupByArgs} args - Group by arguments.
     * @example
     * // Group by city, order by createdAt, get count
     * const result = await prisma.user.groupBy({
     *   by: ['city', 'createdAt'],
     *   orderBy: {
     *     createdAt: true
     *   },
     *   _count: {
     *     _all: true
     *   },
     * })
     * 
    **/
    groupBy<
      T extends StreakGroupByArgs,
      HasSelectOrTake extends Or<
        Extends<'skip', Keys<T>>,
        Extends<'take', Keys<T>>
      >,
      OrderByArg extends True extends HasSelectOrTake
        ? { orderBy: StreakGroupByArgs['orderBy'] }
        : { orderBy?: StreakGroupByArgs['orderBy'] },
      OrderFields extends ExcludeUnderscoreKeys<Keys<MaybeTupleToUnion<T['orderBy']>>>,
      ByFields extends MaybeTupleToUnion<T['by']>,
      ByValid extends Has<ByFields, OrderFields>,
      HavingFields extends GetHavingFields<T['having']>,
      HavingValid extends Has<ByFields, HavingFields>,
      ByEmpty extends T['by'] extends never[] ? True : False,
      InputErrors extends ByEmpty extends True
      ? `Error: "by" must not be empty.`
      : HavingValid extends False
      ? {
          [P in HavingFields]: P extends ByFields
            ? never
            : P extends string
            ? `Error: Field "${P}" used in "having" needs to be provided in "by".`
            : [
                Error,
                'Field ',
                P,
                ` in "having" needs to be provided in "by"`,
              ]
        }[HavingFields]
      : 'take' extends Keys<T>
      ? 'orderBy' extends Keys<T>
        ? ByValid extends True
          ? {}
          : {
              [P in OrderFields]: P extends ByFields
                ? never
                : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
            }[OrderFields]
        : 'Error: If you provide "take", you also need to provide "orderBy"'
      : 'skip' extends Keys<T>
      ? 'orderBy' extends Keys<T>
        ? ByValid extends True
          ? {}
          : {
              [P in OrderFields]: P extends ByFields
                ? never
                : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
            }[OrderFields]
        : 'Error: If you provide "skip", you also need to provide "orderBy"'
      : ByValid extends True
      ? {}
      : {
          [P in OrderFields]: P extends ByFields
            ? never
            : `Error: Field "${P}" in "orderBy" needs to be provided in "by"`
        }[OrderFields]
    >(args: SubsetIntersection<T, StreakGroupByArgs, OrderByArg> & InputErrors): {} extends InputErrors ? GetStreakGroupByPayload<T> : Prisma.PrismaPromise<InputErrors>
  /**
   * Fields of the Streak model
   */
  readonly fields: StreakFieldRefs;
  }

  /**
   * The delegate class that acts as a "Promise-like" for Streak.
   * Why is this prefixed with `Prisma__`?
   * Because we want to prevent naming conflicts as mentioned in
   * https://github.com/prisma/prisma-client-js/issues/707
   */
  export interface Prisma__StreakClient<T, Null = never, ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> extends Prisma.PrismaPromise<T> {
    readonly [Symbol.toStringTag]: "PrismaPromise"
    user<T extends UserDefaultArgs<ExtArgs> = {}>(args?: Subset<T, UserDefaultArgs<ExtArgs>>): Prisma__UserClient<$Result.GetResult<Prisma.$UserPayload<ExtArgs>, T, "findUniqueOrThrow"> | Null, Null, ExtArgs>
    challenge<T extends ChallengeDefaultArgs<ExtArgs> = {}>(args?: Subset<T, ChallengeDefaultArgs<ExtArgs>>): Prisma__ChallengeClient<$Result.GetResult<Prisma.$ChallengePayload<ExtArgs>, T, "findUniqueOrThrow"> | Null, Null, ExtArgs>
    /**
     * Attaches callbacks for the resolution and/or rejection of the Promise.
     * @param onfulfilled The callback to execute when the Promise is resolved.
     * @param onrejected The callback to execute when the Promise is rejected.
     * @returns A Promise for the completion of which ever callback is executed.
     */
    then<TResult1 = T, TResult2 = never>(onfulfilled?: ((value: T) => TResult1 | PromiseLike<TResult1>) | undefined | null, onrejected?: ((reason: any) => TResult2 | PromiseLike<TResult2>) | undefined | null): $Utils.JsPromise<TResult1 | TResult2>
    /**
     * Attaches a callback for only the rejection of the Promise.
     * @param onrejected The callback to execute when the Promise is rejected.
     * @returns A Promise for the completion of the callback.
     */
    catch<TResult = never>(onrejected?: ((reason: any) => TResult | PromiseLike<TResult>) | undefined | null): $Utils.JsPromise<T | TResult>
    /**
     * Attaches a callback that is invoked when the Promise is settled (fulfilled or rejected). The
     * resolved value cannot be modified from the callback.
     * @param onfinally The callback to execute when the Promise is settled (fulfilled or rejected).
     * @returns A Promise for the completion of the callback.
     */
    finally(onfinally?: (() => void) | undefined | null): $Utils.JsPromise<T>
  }




  /**
   * Fields of the Streak model
   */ 
  interface StreakFieldRefs {
    readonly id: FieldRef<"Streak", 'String'>
    readonly userId: FieldRef<"Streak", 'String'>
    readonly challengeId: FieldRef<"Streak", 'String'>
    readonly length: FieldRef<"Streak", 'Int'>
    readonly missedDays: FieldRef<"Streak", 'Int'>
    readonly freezesUsed: FieldRef<"Streak", 'Int'>
    readonly lastUpdated: FieldRef<"Streak", 'DateTime'>
    readonly isActive: FieldRef<"Streak", 'Boolean'>
    readonly lastCompletedDay: FieldRef<"Streak", 'Int'>
  }
    

  // Custom InputTypes
  /**
   * Streak findUnique
   */
  export type StreakFindUniqueArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
    /**
     * Filter, which Streak to fetch.
     */
    where: StreakWhereUniqueInput
  }

  /**
   * Streak findUniqueOrThrow
   */
  export type StreakFindUniqueOrThrowArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
    /**
     * Filter, which Streak to fetch.
     */
    where: StreakWhereUniqueInput
  }

  /**
   * Streak findFirst
   */
  export type StreakFindFirstArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
    /**
     * Filter, which Streak to fetch.
     */
    where?: StreakWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Streaks to fetch.
     */
    orderBy?: StreakOrderByWithRelationInput | StreakOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for searching for Streaks.
     */
    cursor?: StreakWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Streaks from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Streaks.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/distinct Distinct Docs}
     * 
     * Filter by unique combinations of Streaks.
     */
    distinct?: StreakScalarFieldEnum | StreakScalarFieldEnum[]
  }

  /**
   * Streak findFirstOrThrow
   */
  export type StreakFindFirstOrThrowArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
    /**
     * Filter, which Streak to fetch.
     */
    where?: StreakWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Streaks to fetch.
     */
    orderBy?: StreakOrderByWithRelationInput | StreakOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for searching for Streaks.
     */
    cursor?: StreakWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Streaks from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Streaks.
     */
    skip?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/distinct Distinct Docs}
     * 
     * Filter by unique combinations of Streaks.
     */
    distinct?: StreakScalarFieldEnum | StreakScalarFieldEnum[]
  }

  /**
   * Streak findMany
   */
  export type StreakFindManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
    /**
     * Filter, which Streaks to fetch.
     */
    where?: StreakWhereInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/sorting Sorting Docs}
     * 
     * Determine the order of Streaks to fetch.
     */
    orderBy?: StreakOrderByWithRelationInput | StreakOrderByWithRelationInput[]
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination#cursor-based-pagination Cursor Docs}
     * 
     * Sets the position for listing Streaks.
     */
    cursor?: StreakWhereUniqueInput
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Take `±n` Streaks from the position of the cursor.
     */
    take?: number
    /**
     * {@link https://www.prisma.io/docs/concepts/components/prisma-client/pagination Pagination Docs}
     * 
     * Skip the first `n` Streaks.
     */
    skip?: number
    distinct?: StreakScalarFieldEnum | StreakScalarFieldEnum[]
  }

  /**
   * Streak create
   */
  export type StreakCreateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
    /**
     * The data needed to create a Streak.
     */
    data: XOR<StreakCreateInput, StreakUncheckedCreateInput>
  }

  /**
   * Streak createMany
   */
  export type StreakCreateManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * The data used to create many Streaks.
     */
    data: StreakCreateManyInput | StreakCreateManyInput[]
    skipDuplicates?: boolean
  }

  /**
   * Streak createManyAndReturn
   */
  export type StreakCreateManyAndReturnArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelectCreateManyAndReturn<ExtArgs> | null
    /**
     * The data used to create many Streaks.
     */
    data: StreakCreateManyInput | StreakCreateManyInput[]
    skipDuplicates?: boolean
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakIncludeCreateManyAndReturn<ExtArgs> | null
  }

  /**
   * Streak update
   */
  export type StreakUpdateArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
    /**
     * The data needed to update a Streak.
     */
    data: XOR<StreakUpdateInput, StreakUncheckedUpdateInput>
    /**
     * Choose, which Streak to update.
     */
    where: StreakWhereUniqueInput
  }

  /**
   * Streak updateMany
   */
  export type StreakUpdateManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * The data used to update Streaks.
     */
    data: XOR<StreakUpdateManyMutationInput, StreakUncheckedUpdateManyInput>
    /**
     * Filter which Streaks to update
     */
    where?: StreakWhereInput
  }

  /**
   * Streak upsert
   */
  export type StreakUpsertArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
    /**
     * The filter to search for the Streak to update in case it exists.
     */
    where: StreakWhereUniqueInput
    /**
     * In case the Streak found by the `where` argument doesn't exist, create a new Streak with this data.
     */
    create: XOR<StreakCreateInput, StreakUncheckedCreateInput>
    /**
     * In case the Streak was found with the provided `where` argument, update it with this data.
     */
    update: XOR<StreakUpdateInput, StreakUncheckedUpdateInput>
  }

  /**
   * Streak delete
   */
  export type StreakDeleteArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
    /**
     * Filter which Streak to delete.
     */
    where: StreakWhereUniqueInput
  }

  /**
   * Streak deleteMany
   */
  export type StreakDeleteManyArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Filter which Streaks to delete
     */
    where?: StreakWhereInput
  }

  /**
   * Streak without action
   */
  export type StreakDefaultArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = {
    /**
     * Select specific fields to fetch from the Streak
     */
    select?: StreakSelect<ExtArgs> | null
    /**
     * Choose, which related nodes to fetch as well
     */
    include?: StreakInclude<ExtArgs> | null
  }


  /**
   * Enums
   */

  export const TransactionIsolationLevel: {
    ReadUncommitted: 'ReadUncommitted',
    ReadCommitted: 'ReadCommitted',
    RepeatableRead: 'RepeatableRead',
    Serializable: 'Serializable'
  };

  export type TransactionIsolationLevel = (typeof TransactionIsolationLevel)[keyof typeof TransactionIsolationLevel]


  export const UserScalarFieldEnum: {
    id: 'id',
    email: 'email',
    createdAt: 'createdAt'
  };

  export type UserScalarFieldEnum = (typeof UserScalarFieldEnum)[keyof typeof UserScalarFieldEnum]


  export const ChallengeScalarFieldEnum: {
    id: 'id',
    userId: 'userId',
    name: 'name',
    description: 'description',
    type: 'type',
    category: 'category',
    duration: 'duration',
    startDate: 'startDate',
    endDate: 'endDate',
    targetValue: 'targetValue',
    unit: 'unit',
    color: 'color',
    icon: 'icon',
    isActive: 'isActive',
    createdAt: 'createdAt',
    updatedAt: 'updatedAt'
  };

  export type ChallengeScalarFieldEnum = (typeof ChallengeScalarFieldEnum)[keyof typeof ChallengeScalarFieldEnum]


  export const WaterEntryScalarFieldEnum: {
    id: 'id',
    userId: 'userId',
    challengeId: 'challengeId',
    date: 'date',
    amount: 'amount',
    targetAmount: 'targetAmount',
    completed: 'completed',
    createdAt: 'createdAt',
    updatedAt: 'updatedAt'
  };

  export type WaterEntryScalarFieldEnum = (typeof WaterEntryScalarFieldEnum)[keyof typeof WaterEntryScalarFieldEnum]


  export const DailyEntryScalarFieldEnum: {
    id: 'id',
    userId: 'userId',
    challengeId: 'challengeId',
    dayNumber: 'dayNumber',
    date: 'date',
    completed: 'completed',
    completedAt: 'completedAt',
    notes: 'notes',
    value: 'value'
  };

  export type DailyEntryScalarFieldEnum = (typeof DailyEntryScalarFieldEnum)[keyof typeof DailyEntryScalarFieldEnum]


  export const StreakScalarFieldEnum: {
    id: 'id',
    userId: 'userId',
    challengeId: 'challengeId',
    length: 'length',
    missedDays: 'missedDays',
    freezesUsed: 'freezesUsed',
    lastUpdated: 'lastUpdated',
    isActive: 'isActive',
    lastCompletedDay: 'lastCompletedDay'
  };

  export type StreakScalarFieldEnum = (typeof StreakScalarFieldEnum)[keyof typeof StreakScalarFieldEnum]


  export const SortOrder: {
    asc: 'asc',
    desc: 'desc'
  };

  export type SortOrder = (typeof SortOrder)[keyof typeof SortOrder]


  export const QueryMode: {
    default: 'default',
    insensitive: 'insensitive'
  };

  export type QueryMode = (typeof QueryMode)[keyof typeof QueryMode]


  export const NullsOrder: {
    first: 'first',
    last: 'last'
  };

  export type NullsOrder = (typeof NullsOrder)[keyof typeof NullsOrder]


  /**
   * Field references 
   */


  /**
   * Reference to a field of type 'String'
   */
  export type StringFieldRefInput<$PrismaModel> = FieldRefInputType<$PrismaModel, 'String'>
    


  /**
   * Reference to a field of type 'String[]'
   */
  export type ListStringFieldRefInput<$PrismaModel> = FieldRefInputType<$PrismaModel, 'String[]'>
    


  /**
   * Reference to a field of type 'DateTime'
   */
  export type DateTimeFieldRefInput<$PrismaModel> = FieldRefInputType<$PrismaModel, 'DateTime'>
    


  /**
   * Reference to a field of type 'DateTime[]'
   */
  export type ListDateTimeFieldRefInput<$PrismaModel> = FieldRefInputType<$PrismaModel, 'DateTime[]'>
    


  /**
   * Reference to a field of type 'Int'
   */
  export type IntFieldRefInput<$PrismaModel> = FieldRefInputType<$PrismaModel, 'Int'>
    


  /**
   * Reference to a field of type 'Int[]'
   */
  export type ListIntFieldRefInput<$PrismaModel> = FieldRefInputType<$PrismaModel, 'Int[]'>
    


  /**
   * Reference to a field of type 'Float'
   */
  export type FloatFieldRefInput<$PrismaModel> = FieldRefInputType<$PrismaModel, 'Float'>
    


  /**
   * Reference to a field of type 'Float[]'
   */
  export type ListFloatFieldRefInput<$PrismaModel> = FieldRefInputType<$PrismaModel, 'Float[]'>
    


  /**
   * Reference to a field of type 'Boolean'
   */
  export type BooleanFieldRefInput<$PrismaModel> = FieldRefInputType<$PrismaModel, 'Boolean'>
    
  /**
   * Deep Input Types
   */


  export type UserWhereInput = {
    AND?: UserWhereInput | UserWhereInput[]
    OR?: UserWhereInput[]
    NOT?: UserWhereInput | UserWhereInput[]
    id?: StringFilter<"User"> | string
    email?: StringFilter<"User"> | string
    createdAt?: DateTimeFilter<"User"> | Date | string
    challenges?: ChallengeListRelationFilter
    streaks?: StreakListRelationFilter
    dailyEntries?: DailyEntryListRelationFilter
    waterEntries?: WaterEntryListRelationFilter
  }

  export type UserOrderByWithRelationInput = {
    id?: SortOrder
    email?: SortOrder
    createdAt?: SortOrder
    challenges?: ChallengeOrderByRelationAggregateInput
    streaks?: StreakOrderByRelationAggregateInput
    dailyEntries?: DailyEntryOrderByRelationAggregateInput
    waterEntries?: WaterEntryOrderByRelationAggregateInput
  }

  export type UserWhereUniqueInput = Prisma.AtLeast<{
    id?: string
    email?: string
    AND?: UserWhereInput | UserWhereInput[]
    OR?: UserWhereInput[]
    NOT?: UserWhereInput | UserWhereInput[]
    createdAt?: DateTimeFilter<"User"> | Date | string
    challenges?: ChallengeListRelationFilter
    streaks?: StreakListRelationFilter
    dailyEntries?: DailyEntryListRelationFilter
    waterEntries?: WaterEntryListRelationFilter
  }, "id" | "email">

  export type UserOrderByWithAggregationInput = {
    id?: SortOrder
    email?: SortOrder
    createdAt?: SortOrder
    _count?: UserCountOrderByAggregateInput
    _max?: UserMaxOrderByAggregateInput
    _min?: UserMinOrderByAggregateInput
  }

  export type UserScalarWhereWithAggregatesInput = {
    AND?: UserScalarWhereWithAggregatesInput | UserScalarWhereWithAggregatesInput[]
    OR?: UserScalarWhereWithAggregatesInput[]
    NOT?: UserScalarWhereWithAggregatesInput | UserScalarWhereWithAggregatesInput[]
    id?: StringWithAggregatesFilter<"User"> | string
    email?: StringWithAggregatesFilter<"User"> | string
    createdAt?: DateTimeWithAggregatesFilter<"User"> | Date | string
  }

  export type ChallengeWhereInput = {
    AND?: ChallengeWhereInput | ChallengeWhereInput[]
    OR?: ChallengeWhereInput[]
    NOT?: ChallengeWhereInput | ChallengeWhereInput[]
    id?: StringFilter<"Challenge"> | string
    userId?: StringFilter<"Challenge"> | string
    name?: StringFilter<"Challenge"> | string
    description?: StringNullableFilter<"Challenge"> | string | null
    type?: StringFilter<"Challenge"> | string
    category?: StringFilter<"Challenge"> | string
    duration?: IntFilter<"Challenge"> | number
    startDate?: DateTimeFilter<"Challenge"> | Date | string
    endDate?: DateTimeFilter<"Challenge"> | Date | string
    targetValue?: FloatNullableFilter<"Challenge"> | number | null
    unit?: StringNullableFilter<"Challenge"> | string | null
    color?: StringNullableFilter<"Challenge"> | string | null
    icon?: StringNullableFilter<"Challenge"> | string | null
    isActive?: BoolFilter<"Challenge"> | boolean
    createdAt?: DateTimeFilter<"Challenge"> | Date | string
    updatedAt?: DateTimeFilter<"Challenge"> | Date | string
    user?: XOR<UserRelationFilter, UserWhereInput>
    dailyTasks?: DailyEntryListRelationFilter
    streaks?: StreakListRelationFilter
    waterEntries?: WaterEntryListRelationFilter
  }

  export type ChallengeOrderByWithRelationInput = {
    id?: SortOrder
    userId?: SortOrder
    name?: SortOrder
    description?: SortOrderInput | SortOrder
    type?: SortOrder
    category?: SortOrder
    duration?: SortOrder
    startDate?: SortOrder
    endDate?: SortOrder
    targetValue?: SortOrderInput | SortOrder
    unit?: SortOrderInput | SortOrder
    color?: SortOrderInput | SortOrder
    icon?: SortOrderInput | SortOrder
    isActive?: SortOrder
    createdAt?: SortOrder
    updatedAt?: SortOrder
    user?: UserOrderByWithRelationInput
    dailyTasks?: DailyEntryOrderByRelationAggregateInput
    streaks?: StreakOrderByRelationAggregateInput
    waterEntries?: WaterEntryOrderByRelationAggregateInput
  }

  export type ChallengeWhereUniqueInput = Prisma.AtLeast<{
    id?: string
    AND?: ChallengeWhereInput | ChallengeWhereInput[]
    OR?: ChallengeWhereInput[]
    NOT?: ChallengeWhereInput | ChallengeWhereInput[]
    userId?: StringFilter<"Challenge"> | string
    name?: StringFilter<"Challenge"> | string
    description?: StringNullableFilter<"Challenge"> | string | null
    type?: StringFilter<"Challenge"> | string
    category?: StringFilter<"Challenge"> | string
    duration?: IntFilter<"Challenge"> | number
    startDate?: DateTimeFilter<"Challenge"> | Date | string
    endDate?: DateTimeFilter<"Challenge"> | Date | string
    targetValue?: FloatNullableFilter<"Challenge"> | number | null
    unit?: StringNullableFilter<"Challenge"> | string | null
    color?: StringNullableFilter<"Challenge"> | string | null
    icon?: StringNullableFilter<"Challenge"> | string | null
    isActive?: BoolFilter<"Challenge"> | boolean
    createdAt?: DateTimeFilter<"Challenge"> | Date | string
    updatedAt?: DateTimeFilter<"Challenge"> | Date | string
    user?: XOR<UserRelationFilter, UserWhereInput>
    dailyTasks?: DailyEntryListRelationFilter
    streaks?: StreakListRelationFilter
    waterEntries?: WaterEntryListRelationFilter
  }, "id">

  export type ChallengeOrderByWithAggregationInput = {
    id?: SortOrder
    userId?: SortOrder
    name?: SortOrder
    description?: SortOrderInput | SortOrder
    type?: SortOrder
    category?: SortOrder
    duration?: SortOrder
    startDate?: SortOrder
    endDate?: SortOrder
    targetValue?: SortOrderInput | SortOrder
    unit?: SortOrderInput | SortOrder
    color?: SortOrderInput | SortOrder
    icon?: SortOrderInput | SortOrder
    isActive?: SortOrder
    createdAt?: SortOrder
    updatedAt?: SortOrder
    _count?: ChallengeCountOrderByAggregateInput
    _avg?: ChallengeAvgOrderByAggregateInput
    _max?: ChallengeMaxOrderByAggregateInput
    _min?: ChallengeMinOrderByAggregateInput
    _sum?: ChallengeSumOrderByAggregateInput
  }

  export type ChallengeScalarWhereWithAggregatesInput = {
    AND?: ChallengeScalarWhereWithAggregatesInput | ChallengeScalarWhereWithAggregatesInput[]
    OR?: ChallengeScalarWhereWithAggregatesInput[]
    NOT?: ChallengeScalarWhereWithAggregatesInput | ChallengeScalarWhereWithAggregatesInput[]
    id?: StringWithAggregatesFilter<"Challenge"> | string
    userId?: StringWithAggregatesFilter<"Challenge"> | string
    name?: StringWithAggregatesFilter<"Challenge"> | string
    description?: StringNullableWithAggregatesFilter<"Challenge"> | string | null
    type?: StringWithAggregatesFilter<"Challenge"> | string
    category?: StringWithAggregatesFilter<"Challenge"> | string
    duration?: IntWithAggregatesFilter<"Challenge"> | number
    startDate?: DateTimeWithAggregatesFilter<"Challenge"> | Date | string
    endDate?: DateTimeWithAggregatesFilter<"Challenge"> | Date | string
    targetValue?: FloatNullableWithAggregatesFilter<"Challenge"> | number | null
    unit?: StringNullableWithAggregatesFilter<"Challenge"> | string | null
    color?: StringNullableWithAggregatesFilter<"Challenge"> | string | null
    icon?: StringNullableWithAggregatesFilter<"Challenge"> | string | null
    isActive?: BoolWithAggregatesFilter<"Challenge"> | boolean
    createdAt?: DateTimeWithAggregatesFilter<"Challenge"> | Date | string
    updatedAt?: DateTimeWithAggregatesFilter<"Challenge"> | Date | string
  }

  export type WaterEntryWhereInput = {
    AND?: WaterEntryWhereInput | WaterEntryWhereInput[]
    OR?: WaterEntryWhereInput[]
    NOT?: WaterEntryWhereInput | WaterEntryWhereInput[]
    id?: StringFilter<"WaterEntry"> | string
    userId?: StringFilter<"WaterEntry"> | string
    challengeId?: StringFilter<"WaterEntry"> | string
    date?: DateTimeFilter<"WaterEntry"> | Date | string
    amount?: FloatFilter<"WaterEntry"> | number
    targetAmount?: FloatFilter<"WaterEntry"> | number
    completed?: BoolFilter<"WaterEntry"> | boolean
    createdAt?: DateTimeFilter<"WaterEntry"> | Date | string
    updatedAt?: DateTimeFilter<"WaterEntry"> | Date | string
    user?: XOR<UserRelationFilter, UserWhereInput>
    challenge?: XOR<ChallengeRelationFilter, ChallengeWhereInput>
  }

  export type WaterEntryOrderByWithRelationInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    date?: SortOrder
    amount?: SortOrder
    targetAmount?: SortOrder
    completed?: SortOrder
    createdAt?: SortOrder
    updatedAt?: SortOrder
    user?: UserOrderByWithRelationInput
    challenge?: ChallengeOrderByWithRelationInput
  }

  export type WaterEntryWhereUniqueInput = Prisma.AtLeast<{
    id?: string
    userId_challengeId_date?: WaterEntryUserIdChallengeIdDateCompoundUniqueInput
    AND?: WaterEntryWhereInput | WaterEntryWhereInput[]
    OR?: WaterEntryWhereInput[]
    NOT?: WaterEntryWhereInput | WaterEntryWhereInput[]
    userId?: StringFilter<"WaterEntry"> | string
    challengeId?: StringFilter<"WaterEntry"> | string
    date?: DateTimeFilter<"WaterEntry"> | Date | string
    amount?: FloatFilter<"WaterEntry"> | number
    targetAmount?: FloatFilter<"WaterEntry"> | number
    completed?: BoolFilter<"WaterEntry"> | boolean
    createdAt?: DateTimeFilter<"WaterEntry"> | Date | string
    updatedAt?: DateTimeFilter<"WaterEntry"> | Date | string
    user?: XOR<UserRelationFilter, UserWhereInput>
    challenge?: XOR<ChallengeRelationFilter, ChallengeWhereInput>
  }, "id" | "userId_challengeId_date">

  export type WaterEntryOrderByWithAggregationInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    date?: SortOrder
    amount?: SortOrder
    targetAmount?: SortOrder
    completed?: SortOrder
    createdAt?: SortOrder
    updatedAt?: SortOrder
    _count?: WaterEntryCountOrderByAggregateInput
    _avg?: WaterEntryAvgOrderByAggregateInput
    _max?: WaterEntryMaxOrderByAggregateInput
    _min?: WaterEntryMinOrderByAggregateInput
    _sum?: WaterEntrySumOrderByAggregateInput
  }

  export type WaterEntryScalarWhereWithAggregatesInput = {
    AND?: WaterEntryScalarWhereWithAggregatesInput | WaterEntryScalarWhereWithAggregatesInput[]
    OR?: WaterEntryScalarWhereWithAggregatesInput[]
    NOT?: WaterEntryScalarWhereWithAggregatesInput | WaterEntryScalarWhereWithAggregatesInput[]
    id?: StringWithAggregatesFilter<"WaterEntry"> | string
    userId?: StringWithAggregatesFilter<"WaterEntry"> | string
    challengeId?: StringWithAggregatesFilter<"WaterEntry"> | string
    date?: DateTimeWithAggregatesFilter<"WaterEntry"> | Date | string
    amount?: FloatWithAggregatesFilter<"WaterEntry"> | number
    targetAmount?: FloatWithAggregatesFilter<"WaterEntry"> | number
    completed?: BoolWithAggregatesFilter<"WaterEntry"> | boolean
    createdAt?: DateTimeWithAggregatesFilter<"WaterEntry"> | Date | string
    updatedAt?: DateTimeWithAggregatesFilter<"WaterEntry"> | Date | string
  }

  export type DailyEntryWhereInput = {
    AND?: DailyEntryWhereInput | DailyEntryWhereInput[]
    OR?: DailyEntryWhereInput[]
    NOT?: DailyEntryWhereInput | DailyEntryWhereInput[]
    id?: StringFilter<"DailyEntry"> | string
    userId?: StringFilter<"DailyEntry"> | string
    challengeId?: StringFilter<"DailyEntry"> | string
    dayNumber?: IntFilter<"DailyEntry"> | number
    date?: DateTimeFilter<"DailyEntry"> | Date | string
    completed?: BoolFilter<"DailyEntry"> | boolean
    completedAt?: DateTimeNullableFilter<"DailyEntry"> | Date | string | null
    notes?: StringNullableFilter<"DailyEntry"> | string | null
    value?: FloatNullableFilter<"DailyEntry"> | number | null
    user?: XOR<UserRelationFilter, UserWhereInput>
    challenge?: XOR<ChallengeRelationFilter, ChallengeWhereInput>
  }

  export type DailyEntryOrderByWithRelationInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    dayNumber?: SortOrder
    date?: SortOrder
    completed?: SortOrder
    completedAt?: SortOrderInput | SortOrder
    notes?: SortOrderInput | SortOrder
    value?: SortOrderInput | SortOrder
    user?: UserOrderByWithRelationInput
    challenge?: ChallengeOrderByWithRelationInput
  }

  export type DailyEntryWhereUniqueInput = Prisma.AtLeast<{
    id?: string
    userId_challengeId_dayNumber?: DailyEntryUserIdChallengeIdDayNumberCompoundUniqueInput
    userId_challengeId_date?: DailyEntryUserIdChallengeIdDateCompoundUniqueInput
    AND?: DailyEntryWhereInput | DailyEntryWhereInput[]
    OR?: DailyEntryWhereInput[]
    NOT?: DailyEntryWhereInput | DailyEntryWhereInput[]
    userId?: StringFilter<"DailyEntry"> | string
    challengeId?: StringFilter<"DailyEntry"> | string
    dayNumber?: IntFilter<"DailyEntry"> | number
    date?: DateTimeFilter<"DailyEntry"> | Date | string
    completed?: BoolFilter<"DailyEntry"> | boolean
    completedAt?: DateTimeNullableFilter<"DailyEntry"> | Date | string | null
    notes?: StringNullableFilter<"DailyEntry"> | string | null
    value?: FloatNullableFilter<"DailyEntry"> | number | null
    user?: XOR<UserRelationFilter, UserWhereInput>
    challenge?: XOR<ChallengeRelationFilter, ChallengeWhereInput>
  }, "id" | "userId_challengeId_dayNumber" | "userId_challengeId_date">

  export type DailyEntryOrderByWithAggregationInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    dayNumber?: SortOrder
    date?: SortOrder
    completed?: SortOrder
    completedAt?: SortOrderInput | SortOrder
    notes?: SortOrderInput | SortOrder
    value?: SortOrderInput | SortOrder
    _count?: DailyEntryCountOrderByAggregateInput
    _avg?: DailyEntryAvgOrderByAggregateInput
    _max?: DailyEntryMaxOrderByAggregateInput
    _min?: DailyEntryMinOrderByAggregateInput
    _sum?: DailyEntrySumOrderByAggregateInput
  }

  export type DailyEntryScalarWhereWithAggregatesInput = {
    AND?: DailyEntryScalarWhereWithAggregatesInput | DailyEntryScalarWhereWithAggregatesInput[]
    OR?: DailyEntryScalarWhereWithAggregatesInput[]
    NOT?: DailyEntryScalarWhereWithAggregatesInput | DailyEntryScalarWhereWithAggregatesInput[]
    id?: StringWithAggregatesFilter<"DailyEntry"> | string
    userId?: StringWithAggregatesFilter<"DailyEntry"> | string
    challengeId?: StringWithAggregatesFilter<"DailyEntry"> | string
    dayNumber?: IntWithAggregatesFilter<"DailyEntry"> | number
    date?: DateTimeWithAggregatesFilter<"DailyEntry"> | Date | string
    completed?: BoolWithAggregatesFilter<"DailyEntry"> | boolean
    completedAt?: DateTimeNullableWithAggregatesFilter<"DailyEntry"> | Date | string | null
    notes?: StringNullableWithAggregatesFilter<"DailyEntry"> | string | null
    value?: FloatNullableWithAggregatesFilter<"DailyEntry"> | number | null
  }

  export type StreakWhereInput = {
    AND?: StreakWhereInput | StreakWhereInput[]
    OR?: StreakWhereInput[]
    NOT?: StreakWhereInput | StreakWhereInput[]
    id?: StringFilter<"Streak"> | string
    userId?: StringFilter<"Streak"> | string
    challengeId?: StringFilter<"Streak"> | string
    length?: IntFilter<"Streak"> | number
    missedDays?: IntFilter<"Streak"> | number
    freezesUsed?: IntFilter<"Streak"> | number
    lastUpdated?: DateTimeFilter<"Streak"> | Date | string
    isActive?: BoolFilter<"Streak"> | boolean
    lastCompletedDay?: IntFilter<"Streak"> | number
    user?: XOR<UserRelationFilter, UserWhereInput>
    challenge?: XOR<ChallengeRelationFilter, ChallengeWhereInput>
  }

  export type StreakOrderByWithRelationInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    length?: SortOrder
    missedDays?: SortOrder
    freezesUsed?: SortOrder
    lastUpdated?: SortOrder
    isActive?: SortOrder
    lastCompletedDay?: SortOrder
    user?: UserOrderByWithRelationInput
    challenge?: ChallengeOrderByWithRelationInput
  }

  export type StreakWhereUniqueInput = Prisma.AtLeast<{
    id?: string
    userId_challengeId?: StreakUserIdChallengeIdCompoundUniqueInput
    AND?: StreakWhereInput | StreakWhereInput[]
    OR?: StreakWhereInput[]
    NOT?: StreakWhereInput | StreakWhereInput[]
    userId?: StringFilter<"Streak"> | string
    challengeId?: StringFilter<"Streak"> | string
    length?: IntFilter<"Streak"> | number
    missedDays?: IntFilter<"Streak"> | number
    freezesUsed?: IntFilter<"Streak"> | number
    lastUpdated?: DateTimeFilter<"Streak"> | Date | string
    isActive?: BoolFilter<"Streak"> | boolean
    lastCompletedDay?: IntFilter<"Streak"> | number
    user?: XOR<UserRelationFilter, UserWhereInput>
    challenge?: XOR<ChallengeRelationFilter, ChallengeWhereInput>
  }, "id" | "userId_challengeId">

  export type StreakOrderByWithAggregationInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    length?: SortOrder
    missedDays?: SortOrder
    freezesUsed?: SortOrder
    lastUpdated?: SortOrder
    isActive?: SortOrder
    lastCompletedDay?: SortOrder
    _count?: StreakCountOrderByAggregateInput
    _avg?: StreakAvgOrderByAggregateInput
    _max?: StreakMaxOrderByAggregateInput
    _min?: StreakMinOrderByAggregateInput
    _sum?: StreakSumOrderByAggregateInput
  }

  export type StreakScalarWhereWithAggregatesInput = {
    AND?: StreakScalarWhereWithAggregatesInput | StreakScalarWhereWithAggregatesInput[]
    OR?: StreakScalarWhereWithAggregatesInput[]
    NOT?: StreakScalarWhereWithAggregatesInput | StreakScalarWhereWithAggregatesInput[]
    id?: StringWithAggregatesFilter<"Streak"> | string
    userId?: StringWithAggregatesFilter<"Streak"> | string
    challengeId?: StringWithAggregatesFilter<"Streak"> | string
    length?: IntWithAggregatesFilter<"Streak"> | number
    missedDays?: IntWithAggregatesFilter<"Streak"> | number
    freezesUsed?: IntWithAggregatesFilter<"Streak"> | number
    lastUpdated?: DateTimeWithAggregatesFilter<"Streak"> | Date | string
    isActive?: BoolWithAggregatesFilter<"Streak"> | boolean
    lastCompletedDay?: IntWithAggregatesFilter<"Streak"> | number
  }

  export type UserCreateInput = {
    id?: string
    email: string
    createdAt?: Date | string
    challenges?: ChallengeCreateNestedManyWithoutUserInput
    streaks?: StreakCreateNestedManyWithoutUserInput
    dailyEntries?: DailyEntryCreateNestedManyWithoutUserInput
    waterEntries?: WaterEntryCreateNestedManyWithoutUserInput
  }

  export type UserUncheckedCreateInput = {
    id?: string
    email: string
    createdAt?: Date | string
    challenges?: ChallengeUncheckedCreateNestedManyWithoutUserInput
    streaks?: StreakUncheckedCreateNestedManyWithoutUserInput
    dailyEntries?: DailyEntryUncheckedCreateNestedManyWithoutUserInput
    waterEntries?: WaterEntryUncheckedCreateNestedManyWithoutUserInput
  }

  export type UserUpdateInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    challenges?: ChallengeUpdateManyWithoutUserNestedInput
    streaks?: StreakUpdateManyWithoutUserNestedInput
    dailyEntries?: DailyEntryUpdateManyWithoutUserNestedInput
    waterEntries?: WaterEntryUpdateManyWithoutUserNestedInput
  }

  export type UserUncheckedUpdateInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    challenges?: ChallengeUncheckedUpdateManyWithoutUserNestedInput
    streaks?: StreakUncheckedUpdateManyWithoutUserNestedInput
    dailyEntries?: DailyEntryUncheckedUpdateManyWithoutUserNestedInput
    waterEntries?: WaterEntryUncheckedUpdateManyWithoutUserNestedInput
  }

  export type UserCreateManyInput = {
    id?: string
    email: string
    createdAt?: Date | string
  }

  export type UserUpdateManyMutationInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }

  export type UserUncheckedUpdateManyInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }

  export type ChallengeCreateInput = {
    id?: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    user: UserCreateNestedOneWithoutChallengesInput
    dailyTasks?: DailyEntryCreateNestedManyWithoutChallengeInput
    streaks?: StreakCreateNestedManyWithoutChallengeInput
    waterEntries?: WaterEntryCreateNestedManyWithoutChallengeInput
  }

  export type ChallengeUncheckedCreateInput = {
    id?: string
    userId: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    dailyTasks?: DailyEntryUncheckedCreateNestedManyWithoutChallengeInput
    streaks?: StreakUncheckedCreateNestedManyWithoutChallengeInput
    waterEntries?: WaterEntryUncheckedCreateNestedManyWithoutChallengeInput
  }

  export type ChallengeUpdateInput = {
    id?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    user?: UserUpdateOneRequiredWithoutChallengesNestedInput
    dailyTasks?: DailyEntryUpdateManyWithoutChallengeNestedInput
    streaks?: StreakUpdateManyWithoutChallengeNestedInput
    waterEntries?: WaterEntryUpdateManyWithoutChallengeNestedInput
  }

  export type ChallengeUncheckedUpdateInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    dailyTasks?: DailyEntryUncheckedUpdateManyWithoutChallengeNestedInput
    streaks?: StreakUncheckedUpdateManyWithoutChallengeNestedInput
    waterEntries?: WaterEntryUncheckedUpdateManyWithoutChallengeNestedInput
  }

  export type ChallengeCreateManyInput = {
    id?: string
    userId: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
  }

  export type ChallengeUpdateManyMutationInput = {
    id?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }

  export type ChallengeUncheckedUpdateManyInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }

  export type WaterEntryCreateInput = {
    id?: string
    date?: Date | string
    amount: number
    targetAmount: number
    completed?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    user: UserCreateNestedOneWithoutWaterEntriesInput
    challenge: ChallengeCreateNestedOneWithoutWaterEntriesInput
  }

  export type WaterEntryUncheckedCreateInput = {
    id?: string
    userId: string
    challengeId: string
    date?: Date | string
    amount: number
    targetAmount: number
    completed?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
  }

  export type WaterEntryUpdateInput = {
    id?: StringFieldUpdateOperationsInput | string
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    amount?: FloatFieldUpdateOperationsInput | number
    targetAmount?: FloatFieldUpdateOperationsInput | number
    completed?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    user?: UserUpdateOneRequiredWithoutWaterEntriesNestedInput
    challenge?: ChallengeUpdateOneRequiredWithoutWaterEntriesNestedInput
  }

  export type WaterEntryUncheckedUpdateInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    amount?: FloatFieldUpdateOperationsInput | number
    targetAmount?: FloatFieldUpdateOperationsInput | number
    completed?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }

  export type WaterEntryCreateManyInput = {
    id?: string
    userId: string
    challengeId: string
    date?: Date | string
    amount: number
    targetAmount: number
    completed?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
  }

  export type WaterEntryUpdateManyMutationInput = {
    id?: StringFieldUpdateOperationsInput | string
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    amount?: FloatFieldUpdateOperationsInput | number
    targetAmount?: FloatFieldUpdateOperationsInput | number
    completed?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }

  export type WaterEntryUncheckedUpdateManyInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    amount?: FloatFieldUpdateOperationsInput | number
    targetAmount?: FloatFieldUpdateOperationsInput | number
    completed?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }

  export type DailyEntryCreateInput = {
    id?: string
    dayNumber: number
    date: Date | string
    completed?: boolean
    completedAt?: Date | string | null
    notes?: string | null
    value?: number | null
    user: UserCreateNestedOneWithoutDailyEntriesInput
    challenge: ChallengeCreateNestedOneWithoutDailyTasksInput
  }

  export type DailyEntryUncheckedCreateInput = {
    id?: string
    userId: string
    challengeId: string
    dayNumber: number
    date: Date | string
    completed?: boolean
    completedAt?: Date | string | null
    notes?: string | null
    value?: number | null
  }

  export type DailyEntryUpdateInput = {
    id?: StringFieldUpdateOperationsInput | string
    dayNumber?: IntFieldUpdateOperationsInput | number
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    completed?: BoolFieldUpdateOperationsInput | boolean
    completedAt?: NullableDateTimeFieldUpdateOperationsInput | Date | string | null
    notes?: NullableStringFieldUpdateOperationsInput | string | null
    value?: NullableFloatFieldUpdateOperationsInput | number | null
    user?: UserUpdateOneRequiredWithoutDailyEntriesNestedInput
    challenge?: ChallengeUpdateOneRequiredWithoutDailyTasksNestedInput
  }

  export type DailyEntryUncheckedUpdateInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    dayNumber?: IntFieldUpdateOperationsInput | number
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    completed?: BoolFieldUpdateOperationsInput | boolean
    completedAt?: NullableDateTimeFieldUpdateOperationsInput | Date | string | null
    notes?: NullableStringFieldUpdateOperationsInput | string | null
    value?: NullableFloatFieldUpdateOperationsInput | number | null
  }

  export type DailyEntryCreateManyInput = {
    id?: string
    userId: string
    challengeId: string
    dayNumber: number
    date: Date | string
    completed?: boolean
    completedAt?: Date | string | null
    notes?: string | null
    value?: number | null
  }

  export type DailyEntryUpdateManyMutationInput = {
    id?: StringFieldUpdateOperationsInput | string
    dayNumber?: IntFieldUpdateOperationsInput | number
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    completed?: BoolFieldUpdateOperationsInput | boolean
    completedAt?: NullableDateTimeFieldUpdateOperationsInput | Date | string | null
    notes?: NullableStringFieldUpdateOperationsInput | string | null
    value?: NullableFloatFieldUpdateOperationsInput | number | null
  }

  export type DailyEntryUncheckedUpdateManyInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    dayNumber?: IntFieldUpdateOperationsInput | number
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    completed?: BoolFieldUpdateOperationsInput | boolean
    completedAt?: NullableDateTimeFieldUpdateOperationsInput | Date | string | null
    notes?: NullableStringFieldUpdateOperationsInput | string | null
    value?: NullableFloatFieldUpdateOperationsInput | number | null
  }

  export type StreakCreateInput = {
    id?: string
    length?: number
    missedDays?: number
    freezesUsed?: number
    lastUpdated?: Date | string
    isActive?: boolean
    lastCompletedDay?: number
    user: UserCreateNestedOneWithoutStreaksInput
    challenge: ChallengeCreateNestedOneWithoutStreaksInput
  }

  export type StreakUncheckedCreateInput = {
    id?: string
    userId: string
    challengeId: string
    length?: number
    missedDays?: number
    freezesUsed?: number
    lastUpdated?: Date | string
    isActive?: boolean
    lastCompletedDay?: number
  }

  export type StreakUpdateInput = {
    id?: StringFieldUpdateOperationsInput | string
    length?: IntFieldUpdateOperationsInput | number
    missedDays?: IntFieldUpdateOperationsInput | number
    freezesUsed?: IntFieldUpdateOperationsInput | number
    lastUpdated?: DateTimeFieldUpdateOperationsInput | Date | string
    isActive?: BoolFieldUpdateOperationsInput | boolean
    lastCompletedDay?: IntFieldUpdateOperationsInput | number
    user?: UserUpdateOneRequiredWithoutStreaksNestedInput
    challenge?: ChallengeUpdateOneRequiredWithoutStreaksNestedInput
  }

  export type StreakUncheckedUpdateInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    length?: IntFieldUpdateOperationsInput | number
    missedDays?: IntFieldUpdateOperationsInput | number
    freezesUsed?: IntFieldUpdateOperationsInput | number
    lastUpdated?: DateTimeFieldUpdateOperationsInput | Date | string
    isActive?: BoolFieldUpdateOperationsInput | boolean
    lastCompletedDay?: IntFieldUpdateOperationsInput | number
  }

  export type StreakCreateManyInput = {
    id?: string
    userId: string
    challengeId: string
    length?: number
    missedDays?: number
    freezesUsed?: number
    lastUpdated?: Date | string
    isActive?: boolean
    lastCompletedDay?: number
  }

  export type StreakUpdateManyMutationInput = {
    id?: StringFieldUpdateOperationsInput | string
    length?: IntFieldUpdateOperationsInput | number
    missedDays?: IntFieldUpdateOperationsInput | number
    freezesUsed?: IntFieldUpdateOperationsInput | number
    lastUpdated?: DateTimeFieldUpdateOperationsInput | Date | string
    isActive?: BoolFieldUpdateOperationsInput | boolean
    lastCompletedDay?: IntFieldUpdateOperationsInput | number
  }

  export type StreakUncheckedUpdateManyInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    length?: IntFieldUpdateOperationsInput | number
    missedDays?: IntFieldUpdateOperationsInput | number
    freezesUsed?: IntFieldUpdateOperationsInput | number
    lastUpdated?: DateTimeFieldUpdateOperationsInput | Date | string
    isActive?: BoolFieldUpdateOperationsInput | boolean
    lastCompletedDay?: IntFieldUpdateOperationsInput | number
  }

  export type StringFilter<$PrismaModel = never> = {
    equals?: string | StringFieldRefInput<$PrismaModel>
    in?: string[] | ListStringFieldRefInput<$PrismaModel>
    notIn?: string[] | ListStringFieldRefInput<$PrismaModel>
    lt?: string | StringFieldRefInput<$PrismaModel>
    lte?: string | StringFieldRefInput<$PrismaModel>
    gt?: string | StringFieldRefInput<$PrismaModel>
    gte?: string | StringFieldRefInput<$PrismaModel>
    contains?: string | StringFieldRefInput<$PrismaModel>
    startsWith?: string | StringFieldRefInput<$PrismaModel>
    endsWith?: string | StringFieldRefInput<$PrismaModel>
    mode?: QueryMode
    not?: NestedStringFilter<$PrismaModel> | string
  }

  export type DateTimeFilter<$PrismaModel = never> = {
    equals?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    in?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel>
    notIn?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel>
    lt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    lte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    not?: NestedDateTimeFilter<$PrismaModel> | Date | string
  }

  export type ChallengeListRelationFilter = {
    every?: ChallengeWhereInput
    some?: ChallengeWhereInput
    none?: ChallengeWhereInput
  }

  export type StreakListRelationFilter = {
    every?: StreakWhereInput
    some?: StreakWhereInput
    none?: StreakWhereInput
  }

  export type DailyEntryListRelationFilter = {
    every?: DailyEntryWhereInput
    some?: DailyEntryWhereInput
    none?: DailyEntryWhereInput
  }

  export type WaterEntryListRelationFilter = {
    every?: WaterEntryWhereInput
    some?: WaterEntryWhereInput
    none?: WaterEntryWhereInput
  }

  export type ChallengeOrderByRelationAggregateInput = {
    _count?: SortOrder
  }

  export type StreakOrderByRelationAggregateInput = {
    _count?: SortOrder
  }

  export type DailyEntryOrderByRelationAggregateInput = {
    _count?: SortOrder
  }

  export type WaterEntryOrderByRelationAggregateInput = {
    _count?: SortOrder
  }

  export type UserCountOrderByAggregateInput = {
    id?: SortOrder
    email?: SortOrder
    createdAt?: SortOrder
  }

  export type UserMaxOrderByAggregateInput = {
    id?: SortOrder
    email?: SortOrder
    createdAt?: SortOrder
  }

  export type UserMinOrderByAggregateInput = {
    id?: SortOrder
    email?: SortOrder
    createdAt?: SortOrder
  }

  export type StringWithAggregatesFilter<$PrismaModel = never> = {
    equals?: string | StringFieldRefInput<$PrismaModel>
    in?: string[] | ListStringFieldRefInput<$PrismaModel>
    notIn?: string[] | ListStringFieldRefInput<$PrismaModel>
    lt?: string | StringFieldRefInput<$PrismaModel>
    lte?: string | StringFieldRefInput<$PrismaModel>
    gt?: string | StringFieldRefInput<$PrismaModel>
    gte?: string | StringFieldRefInput<$PrismaModel>
    contains?: string | StringFieldRefInput<$PrismaModel>
    startsWith?: string | StringFieldRefInput<$PrismaModel>
    endsWith?: string | StringFieldRefInput<$PrismaModel>
    mode?: QueryMode
    not?: NestedStringWithAggregatesFilter<$PrismaModel> | string
    _count?: NestedIntFilter<$PrismaModel>
    _min?: NestedStringFilter<$PrismaModel>
    _max?: NestedStringFilter<$PrismaModel>
  }

  export type DateTimeWithAggregatesFilter<$PrismaModel = never> = {
    equals?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    in?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel>
    notIn?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel>
    lt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    lte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    not?: NestedDateTimeWithAggregatesFilter<$PrismaModel> | Date | string
    _count?: NestedIntFilter<$PrismaModel>
    _min?: NestedDateTimeFilter<$PrismaModel>
    _max?: NestedDateTimeFilter<$PrismaModel>
  }

  export type StringNullableFilter<$PrismaModel = never> = {
    equals?: string | StringFieldRefInput<$PrismaModel> | null
    in?: string[] | ListStringFieldRefInput<$PrismaModel> | null
    notIn?: string[] | ListStringFieldRefInput<$PrismaModel> | null
    lt?: string | StringFieldRefInput<$PrismaModel>
    lte?: string | StringFieldRefInput<$PrismaModel>
    gt?: string | StringFieldRefInput<$PrismaModel>
    gte?: string | StringFieldRefInput<$PrismaModel>
    contains?: string | StringFieldRefInput<$PrismaModel>
    startsWith?: string | StringFieldRefInput<$PrismaModel>
    endsWith?: string | StringFieldRefInput<$PrismaModel>
    mode?: QueryMode
    not?: NestedStringNullableFilter<$PrismaModel> | string | null
  }

  export type IntFilter<$PrismaModel = never> = {
    equals?: number | IntFieldRefInput<$PrismaModel>
    in?: number[] | ListIntFieldRefInput<$PrismaModel>
    notIn?: number[] | ListIntFieldRefInput<$PrismaModel>
    lt?: number | IntFieldRefInput<$PrismaModel>
    lte?: number | IntFieldRefInput<$PrismaModel>
    gt?: number | IntFieldRefInput<$PrismaModel>
    gte?: number | IntFieldRefInput<$PrismaModel>
    not?: NestedIntFilter<$PrismaModel> | number
  }

  export type FloatNullableFilter<$PrismaModel = never> = {
    equals?: number | FloatFieldRefInput<$PrismaModel> | null
    in?: number[] | ListFloatFieldRefInput<$PrismaModel> | null
    notIn?: number[] | ListFloatFieldRefInput<$PrismaModel> | null
    lt?: number | FloatFieldRefInput<$PrismaModel>
    lte?: number | FloatFieldRefInput<$PrismaModel>
    gt?: number | FloatFieldRefInput<$PrismaModel>
    gte?: number | FloatFieldRefInput<$PrismaModel>
    not?: NestedFloatNullableFilter<$PrismaModel> | number | null
  }

  export type BoolFilter<$PrismaModel = never> = {
    equals?: boolean | BooleanFieldRefInput<$PrismaModel>
    not?: NestedBoolFilter<$PrismaModel> | boolean
  }

  export type UserRelationFilter = {
    is?: UserWhereInput
    isNot?: UserWhereInput
  }

  export type SortOrderInput = {
    sort: SortOrder
    nulls?: NullsOrder
  }

  export type ChallengeCountOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    name?: SortOrder
    description?: SortOrder
    type?: SortOrder
    category?: SortOrder
    duration?: SortOrder
    startDate?: SortOrder
    endDate?: SortOrder
    targetValue?: SortOrder
    unit?: SortOrder
    color?: SortOrder
    icon?: SortOrder
    isActive?: SortOrder
    createdAt?: SortOrder
    updatedAt?: SortOrder
  }

  export type ChallengeAvgOrderByAggregateInput = {
    duration?: SortOrder
    targetValue?: SortOrder
  }

  export type ChallengeMaxOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    name?: SortOrder
    description?: SortOrder
    type?: SortOrder
    category?: SortOrder
    duration?: SortOrder
    startDate?: SortOrder
    endDate?: SortOrder
    targetValue?: SortOrder
    unit?: SortOrder
    color?: SortOrder
    icon?: SortOrder
    isActive?: SortOrder
    createdAt?: SortOrder
    updatedAt?: SortOrder
  }

  export type ChallengeMinOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    name?: SortOrder
    description?: SortOrder
    type?: SortOrder
    category?: SortOrder
    duration?: SortOrder
    startDate?: SortOrder
    endDate?: SortOrder
    targetValue?: SortOrder
    unit?: SortOrder
    color?: SortOrder
    icon?: SortOrder
    isActive?: SortOrder
    createdAt?: SortOrder
    updatedAt?: SortOrder
  }

  export type ChallengeSumOrderByAggregateInput = {
    duration?: SortOrder
    targetValue?: SortOrder
  }

  export type StringNullableWithAggregatesFilter<$PrismaModel = never> = {
    equals?: string | StringFieldRefInput<$PrismaModel> | null
    in?: string[] | ListStringFieldRefInput<$PrismaModel> | null
    notIn?: string[] | ListStringFieldRefInput<$PrismaModel> | null
    lt?: string | StringFieldRefInput<$PrismaModel>
    lte?: string | StringFieldRefInput<$PrismaModel>
    gt?: string | StringFieldRefInput<$PrismaModel>
    gte?: string | StringFieldRefInput<$PrismaModel>
    contains?: string | StringFieldRefInput<$PrismaModel>
    startsWith?: string | StringFieldRefInput<$PrismaModel>
    endsWith?: string | StringFieldRefInput<$PrismaModel>
    mode?: QueryMode
    not?: NestedStringNullableWithAggregatesFilter<$PrismaModel> | string | null
    _count?: NestedIntNullableFilter<$PrismaModel>
    _min?: NestedStringNullableFilter<$PrismaModel>
    _max?: NestedStringNullableFilter<$PrismaModel>
  }

  export type IntWithAggregatesFilter<$PrismaModel = never> = {
    equals?: number | IntFieldRefInput<$PrismaModel>
    in?: number[] | ListIntFieldRefInput<$PrismaModel>
    notIn?: number[] | ListIntFieldRefInput<$PrismaModel>
    lt?: number | IntFieldRefInput<$PrismaModel>
    lte?: number | IntFieldRefInput<$PrismaModel>
    gt?: number | IntFieldRefInput<$PrismaModel>
    gte?: number | IntFieldRefInput<$PrismaModel>
    not?: NestedIntWithAggregatesFilter<$PrismaModel> | number
    _count?: NestedIntFilter<$PrismaModel>
    _avg?: NestedFloatFilter<$PrismaModel>
    _sum?: NestedIntFilter<$PrismaModel>
    _min?: NestedIntFilter<$PrismaModel>
    _max?: NestedIntFilter<$PrismaModel>
  }

  export type FloatNullableWithAggregatesFilter<$PrismaModel = never> = {
    equals?: number | FloatFieldRefInput<$PrismaModel> | null
    in?: number[] | ListFloatFieldRefInput<$PrismaModel> | null
    notIn?: number[] | ListFloatFieldRefInput<$PrismaModel> | null
    lt?: number | FloatFieldRefInput<$PrismaModel>
    lte?: number | FloatFieldRefInput<$PrismaModel>
    gt?: number | FloatFieldRefInput<$PrismaModel>
    gte?: number | FloatFieldRefInput<$PrismaModel>
    not?: NestedFloatNullableWithAggregatesFilter<$PrismaModel> | number | null
    _count?: NestedIntNullableFilter<$PrismaModel>
    _avg?: NestedFloatNullableFilter<$PrismaModel>
    _sum?: NestedFloatNullableFilter<$PrismaModel>
    _min?: NestedFloatNullableFilter<$PrismaModel>
    _max?: NestedFloatNullableFilter<$PrismaModel>
  }

  export type BoolWithAggregatesFilter<$PrismaModel = never> = {
    equals?: boolean | BooleanFieldRefInput<$PrismaModel>
    not?: NestedBoolWithAggregatesFilter<$PrismaModel> | boolean
    _count?: NestedIntFilter<$PrismaModel>
    _min?: NestedBoolFilter<$PrismaModel>
    _max?: NestedBoolFilter<$PrismaModel>
  }

  export type FloatFilter<$PrismaModel = never> = {
    equals?: number | FloatFieldRefInput<$PrismaModel>
    in?: number[] | ListFloatFieldRefInput<$PrismaModel>
    notIn?: number[] | ListFloatFieldRefInput<$PrismaModel>
    lt?: number | FloatFieldRefInput<$PrismaModel>
    lte?: number | FloatFieldRefInput<$PrismaModel>
    gt?: number | FloatFieldRefInput<$PrismaModel>
    gte?: number | FloatFieldRefInput<$PrismaModel>
    not?: NestedFloatFilter<$PrismaModel> | number
  }

  export type ChallengeRelationFilter = {
    is?: ChallengeWhereInput
    isNot?: ChallengeWhereInput
  }

  export type WaterEntryUserIdChallengeIdDateCompoundUniqueInput = {
    userId: string
    challengeId: string
    date: Date | string
  }

  export type WaterEntryCountOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    date?: SortOrder
    amount?: SortOrder
    targetAmount?: SortOrder
    completed?: SortOrder
    createdAt?: SortOrder
    updatedAt?: SortOrder
  }

  export type WaterEntryAvgOrderByAggregateInput = {
    amount?: SortOrder
    targetAmount?: SortOrder
  }

  export type WaterEntryMaxOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    date?: SortOrder
    amount?: SortOrder
    targetAmount?: SortOrder
    completed?: SortOrder
    createdAt?: SortOrder
    updatedAt?: SortOrder
  }

  export type WaterEntryMinOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    date?: SortOrder
    amount?: SortOrder
    targetAmount?: SortOrder
    completed?: SortOrder
    createdAt?: SortOrder
    updatedAt?: SortOrder
  }

  export type WaterEntrySumOrderByAggregateInput = {
    amount?: SortOrder
    targetAmount?: SortOrder
  }

  export type FloatWithAggregatesFilter<$PrismaModel = never> = {
    equals?: number | FloatFieldRefInput<$PrismaModel>
    in?: number[] | ListFloatFieldRefInput<$PrismaModel>
    notIn?: number[] | ListFloatFieldRefInput<$PrismaModel>
    lt?: number | FloatFieldRefInput<$PrismaModel>
    lte?: number | FloatFieldRefInput<$PrismaModel>
    gt?: number | FloatFieldRefInput<$PrismaModel>
    gte?: number | FloatFieldRefInput<$PrismaModel>
    not?: NestedFloatWithAggregatesFilter<$PrismaModel> | number
    _count?: NestedIntFilter<$PrismaModel>
    _avg?: NestedFloatFilter<$PrismaModel>
    _sum?: NestedFloatFilter<$PrismaModel>
    _min?: NestedFloatFilter<$PrismaModel>
    _max?: NestedFloatFilter<$PrismaModel>
  }

  export type DateTimeNullableFilter<$PrismaModel = never> = {
    equals?: Date | string | DateTimeFieldRefInput<$PrismaModel> | null
    in?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel> | null
    notIn?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel> | null
    lt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    lte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    not?: NestedDateTimeNullableFilter<$PrismaModel> | Date | string | null
  }

  export type DailyEntryUserIdChallengeIdDayNumberCompoundUniqueInput = {
    userId: string
    challengeId: string
    dayNumber: number
  }

  export type DailyEntryUserIdChallengeIdDateCompoundUniqueInput = {
    userId: string
    challengeId: string
    date: Date | string
  }

  export type DailyEntryCountOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    dayNumber?: SortOrder
    date?: SortOrder
    completed?: SortOrder
    completedAt?: SortOrder
    notes?: SortOrder
    value?: SortOrder
  }

  export type DailyEntryAvgOrderByAggregateInput = {
    dayNumber?: SortOrder
    value?: SortOrder
  }

  export type DailyEntryMaxOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    dayNumber?: SortOrder
    date?: SortOrder
    completed?: SortOrder
    completedAt?: SortOrder
    notes?: SortOrder
    value?: SortOrder
  }

  export type DailyEntryMinOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    dayNumber?: SortOrder
    date?: SortOrder
    completed?: SortOrder
    completedAt?: SortOrder
    notes?: SortOrder
    value?: SortOrder
  }

  export type DailyEntrySumOrderByAggregateInput = {
    dayNumber?: SortOrder
    value?: SortOrder
  }

  export type DateTimeNullableWithAggregatesFilter<$PrismaModel = never> = {
    equals?: Date | string | DateTimeFieldRefInput<$PrismaModel> | null
    in?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel> | null
    notIn?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel> | null
    lt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    lte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    not?: NestedDateTimeNullableWithAggregatesFilter<$PrismaModel> | Date | string | null
    _count?: NestedIntNullableFilter<$PrismaModel>
    _min?: NestedDateTimeNullableFilter<$PrismaModel>
    _max?: NestedDateTimeNullableFilter<$PrismaModel>
  }

  export type StreakUserIdChallengeIdCompoundUniqueInput = {
    userId: string
    challengeId: string
  }

  export type StreakCountOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    length?: SortOrder
    missedDays?: SortOrder
    freezesUsed?: SortOrder
    lastUpdated?: SortOrder
    isActive?: SortOrder
    lastCompletedDay?: SortOrder
  }

  export type StreakAvgOrderByAggregateInput = {
    length?: SortOrder
    missedDays?: SortOrder
    freezesUsed?: SortOrder
    lastCompletedDay?: SortOrder
  }

  export type StreakMaxOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    length?: SortOrder
    missedDays?: SortOrder
    freezesUsed?: SortOrder
    lastUpdated?: SortOrder
    isActive?: SortOrder
    lastCompletedDay?: SortOrder
  }

  export type StreakMinOrderByAggregateInput = {
    id?: SortOrder
    userId?: SortOrder
    challengeId?: SortOrder
    length?: SortOrder
    missedDays?: SortOrder
    freezesUsed?: SortOrder
    lastUpdated?: SortOrder
    isActive?: SortOrder
    lastCompletedDay?: SortOrder
  }

  export type StreakSumOrderByAggregateInput = {
    length?: SortOrder
    missedDays?: SortOrder
    freezesUsed?: SortOrder
    lastCompletedDay?: SortOrder
  }

  export type ChallengeCreateNestedManyWithoutUserInput = {
    create?: XOR<ChallengeCreateWithoutUserInput, ChallengeUncheckedCreateWithoutUserInput> | ChallengeCreateWithoutUserInput[] | ChallengeUncheckedCreateWithoutUserInput[]
    connectOrCreate?: ChallengeCreateOrConnectWithoutUserInput | ChallengeCreateOrConnectWithoutUserInput[]
    createMany?: ChallengeCreateManyUserInputEnvelope
    connect?: ChallengeWhereUniqueInput | ChallengeWhereUniqueInput[]
  }

  export type StreakCreateNestedManyWithoutUserInput = {
    create?: XOR<StreakCreateWithoutUserInput, StreakUncheckedCreateWithoutUserInput> | StreakCreateWithoutUserInput[] | StreakUncheckedCreateWithoutUserInput[]
    connectOrCreate?: StreakCreateOrConnectWithoutUserInput | StreakCreateOrConnectWithoutUserInput[]
    createMany?: StreakCreateManyUserInputEnvelope
    connect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
  }

  export type DailyEntryCreateNestedManyWithoutUserInput = {
    create?: XOR<DailyEntryCreateWithoutUserInput, DailyEntryUncheckedCreateWithoutUserInput> | DailyEntryCreateWithoutUserInput[] | DailyEntryUncheckedCreateWithoutUserInput[]
    connectOrCreate?: DailyEntryCreateOrConnectWithoutUserInput | DailyEntryCreateOrConnectWithoutUserInput[]
    createMany?: DailyEntryCreateManyUserInputEnvelope
    connect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
  }

  export type WaterEntryCreateNestedManyWithoutUserInput = {
    create?: XOR<WaterEntryCreateWithoutUserInput, WaterEntryUncheckedCreateWithoutUserInput> | WaterEntryCreateWithoutUserInput[] | WaterEntryUncheckedCreateWithoutUserInput[]
    connectOrCreate?: WaterEntryCreateOrConnectWithoutUserInput | WaterEntryCreateOrConnectWithoutUserInput[]
    createMany?: WaterEntryCreateManyUserInputEnvelope
    connect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
  }

  export type ChallengeUncheckedCreateNestedManyWithoutUserInput = {
    create?: XOR<ChallengeCreateWithoutUserInput, ChallengeUncheckedCreateWithoutUserInput> | ChallengeCreateWithoutUserInput[] | ChallengeUncheckedCreateWithoutUserInput[]
    connectOrCreate?: ChallengeCreateOrConnectWithoutUserInput | ChallengeCreateOrConnectWithoutUserInput[]
    createMany?: ChallengeCreateManyUserInputEnvelope
    connect?: ChallengeWhereUniqueInput | ChallengeWhereUniqueInput[]
  }

  export type StreakUncheckedCreateNestedManyWithoutUserInput = {
    create?: XOR<StreakCreateWithoutUserInput, StreakUncheckedCreateWithoutUserInput> | StreakCreateWithoutUserInput[] | StreakUncheckedCreateWithoutUserInput[]
    connectOrCreate?: StreakCreateOrConnectWithoutUserInput | StreakCreateOrConnectWithoutUserInput[]
    createMany?: StreakCreateManyUserInputEnvelope
    connect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
  }

  export type DailyEntryUncheckedCreateNestedManyWithoutUserInput = {
    create?: XOR<DailyEntryCreateWithoutUserInput, DailyEntryUncheckedCreateWithoutUserInput> | DailyEntryCreateWithoutUserInput[] | DailyEntryUncheckedCreateWithoutUserInput[]
    connectOrCreate?: DailyEntryCreateOrConnectWithoutUserInput | DailyEntryCreateOrConnectWithoutUserInput[]
    createMany?: DailyEntryCreateManyUserInputEnvelope
    connect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
  }

  export type WaterEntryUncheckedCreateNestedManyWithoutUserInput = {
    create?: XOR<WaterEntryCreateWithoutUserInput, WaterEntryUncheckedCreateWithoutUserInput> | WaterEntryCreateWithoutUserInput[] | WaterEntryUncheckedCreateWithoutUserInput[]
    connectOrCreate?: WaterEntryCreateOrConnectWithoutUserInput | WaterEntryCreateOrConnectWithoutUserInput[]
    createMany?: WaterEntryCreateManyUserInputEnvelope
    connect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
  }

  export type StringFieldUpdateOperationsInput = {
    set?: string
  }

  export type DateTimeFieldUpdateOperationsInput = {
    set?: Date | string
  }

  export type ChallengeUpdateManyWithoutUserNestedInput = {
    create?: XOR<ChallengeCreateWithoutUserInput, ChallengeUncheckedCreateWithoutUserInput> | ChallengeCreateWithoutUserInput[] | ChallengeUncheckedCreateWithoutUserInput[]
    connectOrCreate?: ChallengeCreateOrConnectWithoutUserInput | ChallengeCreateOrConnectWithoutUserInput[]
    upsert?: ChallengeUpsertWithWhereUniqueWithoutUserInput | ChallengeUpsertWithWhereUniqueWithoutUserInput[]
    createMany?: ChallengeCreateManyUserInputEnvelope
    set?: ChallengeWhereUniqueInput | ChallengeWhereUniqueInput[]
    disconnect?: ChallengeWhereUniqueInput | ChallengeWhereUniqueInput[]
    delete?: ChallengeWhereUniqueInput | ChallengeWhereUniqueInput[]
    connect?: ChallengeWhereUniqueInput | ChallengeWhereUniqueInput[]
    update?: ChallengeUpdateWithWhereUniqueWithoutUserInput | ChallengeUpdateWithWhereUniqueWithoutUserInput[]
    updateMany?: ChallengeUpdateManyWithWhereWithoutUserInput | ChallengeUpdateManyWithWhereWithoutUserInput[]
    deleteMany?: ChallengeScalarWhereInput | ChallengeScalarWhereInput[]
  }

  export type StreakUpdateManyWithoutUserNestedInput = {
    create?: XOR<StreakCreateWithoutUserInput, StreakUncheckedCreateWithoutUserInput> | StreakCreateWithoutUserInput[] | StreakUncheckedCreateWithoutUserInput[]
    connectOrCreate?: StreakCreateOrConnectWithoutUserInput | StreakCreateOrConnectWithoutUserInput[]
    upsert?: StreakUpsertWithWhereUniqueWithoutUserInput | StreakUpsertWithWhereUniqueWithoutUserInput[]
    createMany?: StreakCreateManyUserInputEnvelope
    set?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    disconnect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    delete?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    connect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    update?: StreakUpdateWithWhereUniqueWithoutUserInput | StreakUpdateWithWhereUniqueWithoutUserInput[]
    updateMany?: StreakUpdateManyWithWhereWithoutUserInput | StreakUpdateManyWithWhereWithoutUserInput[]
    deleteMany?: StreakScalarWhereInput | StreakScalarWhereInput[]
  }

  export type DailyEntryUpdateManyWithoutUserNestedInput = {
    create?: XOR<DailyEntryCreateWithoutUserInput, DailyEntryUncheckedCreateWithoutUserInput> | DailyEntryCreateWithoutUserInput[] | DailyEntryUncheckedCreateWithoutUserInput[]
    connectOrCreate?: DailyEntryCreateOrConnectWithoutUserInput | DailyEntryCreateOrConnectWithoutUserInput[]
    upsert?: DailyEntryUpsertWithWhereUniqueWithoutUserInput | DailyEntryUpsertWithWhereUniqueWithoutUserInput[]
    createMany?: DailyEntryCreateManyUserInputEnvelope
    set?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    disconnect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    delete?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    connect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    update?: DailyEntryUpdateWithWhereUniqueWithoutUserInput | DailyEntryUpdateWithWhereUniqueWithoutUserInput[]
    updateMany?: DailyEntryUpdateManyWithWhereWithoutUserInput | DailyEntryUpdateManyWithWhereWithoutUserInput[]
    deleteMany?: DailyEntryScalarWhereInput | DailyEntryScalarWhereInput[]
  }

  export type WaterEntryUpdateManyWithoutUserNestedInput = {
    create?: XOR<WaterEntryCreateWithoutUserInput, WaterEntryUncheckedCreateWithoutUserInput> | WaterEntryCreateWithoutUserInput[] | WaterEntryUncheckedCreateWithoutUserInput[]
    connectOrCreate?: WaterEntryCreateOrConnectWithoutUserInput | WaterEntryCreateOrConnectWithoutUserInput[]
    upsert?: WaterEntryUpsertWithWhereUniqueWithoutUserInput | WaterEntryUpsertWithWhereUniqueWithoutUserInput[]
    createMany?: WaterEntryCreateManyUserInputEnvelope
    set?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    disconnect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    delete?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    connect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    update?: WaterEntryUpdateWithWhereUniqueWithoutUserInput | WaterEntryUpdateWithWhereUniqueWithoutUserInput[]
    updateMany?: WaterEntryUpdateManyWithWhereWithoutUserInput | WaterEntryUpdateManyWithWhereWithoutUserInput[]
    deleteMany?: WaterEntryScalarWhereInput | WaterEntryScalarWhereInput[]
  }

  export type ChallengeUncheckedUpdateManyWithoutUserNestedInput = {
    create?: XOR<ChallengeCreateWithoutUserInput, ChallengeUncheckedCreateWithoutUserInput> | ChallengeCreateWithoutUserInput[] | ChallengeUncheckedCreateWithoutUserInput[]
    connectOrCreate?: ChallengeCreateOrConnectWithoutUserInput | ChallengeCreateOrConnectWithoutUserInput[]
    upsert?: ChallengeUpsertWithWhereUniqueWithoutUserInput | ChallengeUpsertWithWhereUniqueWithoutUserInput[]
    createMany?: ChallengeCreateManyUserInputEnvelope
    set?: ChallengeWhereUniqueInput | ChallengeWhereUniqueInput[]
    disconnect?: ChallengeWhereUniqueInput | ChallengeWhereUniqueInput[]
    delete?: ChallengeWhereUniqueInput | ChallengeWhereUniqueInput[]
    connect?: ChallengeWhereUniqueInput | ChallengeWhereUniqueInput[]
    update?: ChallengeUpdateWithWhereUniqueWithoutUserInput | ChallengeUpdateWithWhereUniqueWithoutUserInput[]
    updateMany?: ChallengeUpdateManyWithWhereWithoutUserInput | ChallengeUpdateManyWithWhereWithoutUserInput[]
    deleteMany?: ChallengeScalarWhereInput | ChallengeScalarWhereInput[]
  }

  export type StreakUncheckedUpdateManyWithoutUserNestedInput = {
    create?: XOR<StreakCreateWithoutUserInput, StreakUncheckedCreateWithoutUserInput> | StreakCreateWithoutUserInput[] | StreakUncheckedCreateWithoutUserInput[]
    connectOrCreate?: StreakCreateOrConnectWithoutUserInput | StreakCreateOrConnectWithoutUserInput[]
    upsert?: StreakUpsertWithWhereUniqueWithoutUserInput | StreakUpsertWithWhereUniqueWithoutUserInput[]
    createMany?: StreakCreateManyUserInputEnvelope
    set?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    disconnect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    delete?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    connect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    update?: StreakUpdateWithWhereUniqueWithoutUserInput | StreakUpdateWithWhereUniqueWithoutUserInput[]
    updateMany?: StreakUpdateManyWithWhereWithoutUserInput | StreakUpdateManyWithWhereWithoutUserInput[]
    deleteMany?: StreakScalarWhereInput | StreakScalarWhereInput[]
  }

  export type DailyEntryUncheckedUpdateManyWithoutUserNestedInput = {
    create?: XOR<DailyEntryCreateWithoutUserInput, DailyEntryUncheckedCreateWithoutUserInput> | DailyEntryCreateWithoutUserInput[] | DailyEntryUncheckedCreateWithoutUserInput[]
    connectOrCreate?: DailyEntryCreateOrConnectWithoutUserInput | DailyEntryCreateOrConnectWithoutUserInput[]
    upsert?: DailyEntryUpsertWithWhereUniqueWithoutUserInput | DailyEntryUpsertWithWhereUniqueWithoutUserInput[]
    createMany?: DailyEntryCreateManyUserInputEnvelope
    set?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    disconnect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    delete?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    connect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    update?: DailyEntryUpdateWithWhereUniqueWithoutUserInput | DailyEntryUpdateWithWhereUniqueWithoutUserInput[]
    updateMany?: DailyEntryUpdateManyWithWhereWithoutUserInput | DailyEntryUpdateManyWithWhereWithoutUserInput[]
    deleteMany?: DailyEntryScalarWhereInput | DailyEntryScalarWhereInput[]
  }

  export type WaterEntryUncheckedUpdateManyWithoutUserNestedInput = {
    create?: XOR<WaterEntryCreateWithoutUserInput, WaterEntryUncheckedCreateWithoutUserInput> | WaterEntryCreateWithoutUserInput[] | WaterEntryUncheckedCreateWithoutUserInput[]
    connectOrCreate?: WaterEntryCreateOrConnectWithoutUserInput | WaterEntryCreateOrConnectWithoutUserInput[]
    upsert?: WaterEntryUpsertWithWhereUniqueWithoutUserInput | WaterEntryUpsertWithWhereUniqueWithoutUserInput[]
    createMany?: WaterEntryCreateManyUserInputEnvelope
    set?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    disconnect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    delete?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    connect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    update?: WaterEntryUpdateWithWhereUniqueWithoutUserInput | WaterEntryUpdateWithWhereUniqueWithoutUserInput[]
    updateMany?: WaterEntryUpdateManyWithWhereWithoutUserInput | WaterEntryUpdateManyWithWhereWithoutUserInput[]
    deleteMany?: WaterEntryScalarWhereInput | WaterEntryScalarWhereInput[]
  }

  export type UserCreateNestedOneWithoutChallengesInput = {
    create?: XOR<UserCreateWithoutChallengesInput, UserUncheckedCreateWithoutChallengesInput>
    connectOrCreate?: UserCreateOrConnectWithoutChallengesInput
    connect?: UserWhereUniqueInput
  }

  export type DailyEntryCreateNestedManyWithoutChallengeInput = {
    create?: XOR<DailyEntryCreateWithoutChallengeInput, DailyEntryUncheckedCreateWithoutChallengeInput> | DailyEntryCreateWithoutChallengeInput[] | DailyEntryUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: DailyEntryCreateOrConnectWithoutChallengeInput | DailyEntryCreateOrConnectWithoutChallengeInput[]
    createMany?: DailyEntryCreateManyChallengeInputEnvelope
    connect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
  }

  export type StreakCreateNestedManyWithoutChallengeInput = {
    create?: XOR<StreakCreateWithoutChallengeInput, StreakUncheckedCreateWithoutChallengeInput> | StreakCreateWithoutChallengeInput[] | StreakUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: StreakCreateOrConnectWithoutChallengeInput | StreakCreateOrConnectWithoutChallengeInput[]
    createMany?: StreakCreateManyChallengeInputEnvelope
    connect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
  }

  export type WaterEntryCreateNestedManyWithoutChallengeInput = {
    create?: XOR<WaterEntryCreateWithoutChallengeInput, WaterEntryUncheckedCreateWithoutChallengeInput> | WaterEntryCreateWithoutChallengeInput[] | WaterEntryUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: WaterEntryCreateOrConnectWithoutChallengeInput | WaterEntryCreateOrConnectWithoutChallengeInput[]
    createMany?: WaterEntryCreateManyChallengeInputEnvelope
    connect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
  }

  export type DailyEntryUncheckedCreateNestedManyWithoutChallengeInput = {
    create?: XOR<DailyEntryCreateWithoutChallengeInput, DailyEntryUncheckedCreateWithoutChallengeInput> | DailyEntryCreateWithoutChallengeInput[] | DailyEntryUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: DailyEntryCreateOrConnectWithoutChallengeInput | DailyEntryCreateOrConnectWithoutChallengeInput[]
    createMany?: DailyEntryCreateManyChallengeInputEnvelope
    connect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
  }

  export type StreakUncheckedCreateNestedManyWithoutChallengeInput = {
    create?: XOR<StreakCreateWithoutChallengeInput, StreakUncheckedCreateWithoutChallengeInput> | StreakCreateWithoutChallengeInput[] | StreakUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: StreakCreateOrConnectWithoutChallengeInput | StreakCreateOrConnectWithoutChallengeInput[]
    createMany?: StreakCreateManyChallengeInputEnvelope
    connect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
  }

  export type WaterEntryUncheckedCreateNestedManyWithoutChallengeInput = {
    create?: XOR<WaterEntryCreateWithoutChallengeInput, WaterEntryUncheckedCreateWithoutChallengeInput> | WaterEntryCreateWithoutChallengeInput[] | WaterEntryUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: WaterEntryCreateOrConnectWithoutChallengeInput | WaterEntryCreateOrConnectWithoutChallengeInput[]
    createMany?: WaterEntryCreateManyChallengeInputEnvelope
    connect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
  }

  export type NullableStringFieldUpdateOperationsInput = {
    set?: string | null
  }

  export type IntFieldUpdateOperationsInput = {
    set?: number
    increment?: number
    decrement?: number
    multiply?: number
    divide?: number
  }

  export type NullableFloatFieldUpdateOperationsInput = {
    set?: number | null
    increment?: number
    decrement?: number
    multiply?: number
    divide?: number
  }

  export type BoolFieldUpdateOperationsInput = {
    set?: boolean
  }

  export type UserUpdateOneRequiredWithoutChallengesNestedInput = {
    create?: XOR<UserCreateWithoutChallengesInput, UserUncheckedCreateWithoutChallengesInput>
    connectOrCreate?: UserCreateOrConnectWithoutChallengesInput
    upsert?: UserUpsertWithoutChallengesInput
    connect?: UserWhereUniqueInput
    update?: XOR<XOR<UserUpdateToOneWithWhereWithoutChallengesInput, UserUpdateWithoutChallengesInput>, UserUncheckedUpdateWithoutChallengesInput>
  }

  export type DailyEntryUpdateManyWithoutChallengeNestedInput = {
    create?: XOR<DailyEntryCreateWithoutChallengeInput, DailyEntryUncheckedCreateWithoutChallengeInput> | DailyEntryCreateWithoutChallengeInput[] | DailyEntryUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: DailyEntryCreateOrConnectWithoutChallengeInput | DailyEntryCreateOrConnectWithoutChallengeInput[]
    upsert?: DailyEntryUpsertWithWhereUniqueWithoutChallengeInput | DailyEntryUpsertWithWhereUniqueWithoutChallengeInput[]
    createMany?: DailyEntryCreateManyChallengeInputEnvelope
    set?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    disconnect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    delete?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    connect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    update?: DailyEntryUpdateWithWhereUniqueWithoutChallengeInput | DailyEntryUpdateWithWhereUniqueWithoutChallengeInput[]
    updateMany?: DailyEntryUpdateManyWithWhereWithoutChallengeInput | DailyEntryUpdateManyWithWhereWithoutChallengeInput[]
    deleteMany?: DailyEntryScalarWhereInput | DailyEntryScalarWhereInput[]
  }

  export type StreakUpdateManyWithoutChallengeNestedInput = {
    create?: XOR<StreakCreateWithoutChallengeInput, StreakUncheckedCreateWithoutChallengeInput> | StreakCreateWithoutChallengeInput[] | StreakUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: StreakCreateOrConnectWithoutChallengeInput | StreakCreateOrConnectWithoutChallengeInput[]
    upsert?: StreakUpsertWithWhereUniqueWithoutChallengeInput | StreakUpsertWithWhereUniqueWithoutChallengeInput[]
    createMany?: StreakCreateManyChallengeInputEnvelope
    set?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    disconnect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    delete?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    connect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    update?: StreakUpdateWithWhereUniqueWithoutChallengeInput | StreakUpdateWithWhereUniqueWithoutChallengeInput[]
    updateMany?: StreakUpdateManyWithWhereWithoutChallengeInput | StreakUpdateManyWithWhereWithoutChallengeInput[]
    deleteMany?: StreakScalarWhereInput | StreakScalarWhereInput[]
  }

  export type WaterEntryUpdateManyWithoutChallengeNestedInput = {
    create?: XOR<WaterEntryCreateWithoutChallengeInput, WaterEntryUncheckedCreateWithoutChallengeInput> | WaterEntryCreateWithoutChallengeInput[] | WaterEntryUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: WaterEntryCreateOrConnectWithoutChallengeInput | WaterEntryCreateOrConnectWithoutChallengeInput[]
    upsert?: WaterEntryUpsertWithWhereUniqueWithoutChallengeInput | WaterEntryUpsertWithWhereUniqueWithoutChallengeInput[]
    createMany?: WaterEntryCreateManyChallengeInputEnvelope
    set?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    disconnect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    delete?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    connect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    update?: WaterEntryUpdateWithWhereUniqueWithoutChallengeInput | WaterEntryUpdateWithWhereUniqueWithoutChallengeInput[]
    updateMany?: WaterEntryUpdateManyWithWhereWithoutChallengeInput | WaterEntryUpdateManyWithWhereWithoutChallengeInput[]
    deleteMany?: WaterEntryScalarWhereInput | WaterEntryScalarWhereInput[]
  }

  export type DailyEntryUncheckedUpdateManyWithoutChallengeNestedInput = {
    create?: XOR<DailyEntryCreateWithoutChallengeInput, DailyEntryUncheckedCreateWithoutChallengeInput> | DailyEntryCreateWithoutChallengeInput[] | DailyEntryUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: DailyEntryCreateOrConnectWithoutChallengeInput | DailyEntryCreateOrConnectWithoutChallengeInput[]
    upsert?: DailyEntryUpsertWithWhereUniqueWithoutChallengeInput | DailyEntryUpsertWithWhereUniqueWithoutChallengeInput[]
    createMany?: DailyEntryCreateManyChallengeInputEnvelope
    set?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    disconnect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    delete?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    connect?: DailyEntryWhereUniqueInput | DailyEntryWhereUniqueInput[]
    update?: DailyEntryUpdateWithWhereUniqueWithoutChallengeInput | DailyEntryUpdateWithWhereUniqueWithoutChallengeInput[]
    updateMany?: DailyEntryUpdateManyWithWhereWithoutChallengeInput | DailyEntryUpdateManyWithWhereWithoutChallengeInput[]
    deleteMany?: DailyEntryScalarWhereInput | DailyEntryScalarWhereInput[]
  }

  export type StreakUncheckedUpdateManyWithoutChallengeNestedInput = {
    create?: XOR<StreakCreateWithoutChallengeInput, StreakUncheckedCreateWithoutChallengeInput> | StreakCreateWithoutChallengeInput[] | StreakUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: StreakCreateOrConnectWithoutChallengeInput | StreakCreateOrConnectWithoutChallengeInput[]
    upsert?: StreakUpsertWithWhereUniqueWithoutChallengeInput | StreakUpsertWithWhereUniqueWithoutChallengeInput[]
    createMany?: StreakCreateManyChallengeInputEnvelope
    set?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    disconnect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    delete?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    connect?: StreakWhereUniqueInput | StreakWhereUniqueInput[]
    update?: StreakUpdateWithWhereUniqueWithoutChallengeInput | StreakUpdateWithWhereUniqueWithoutChallengeInput[]
    updateMany?: StreakUpdateManyWithWhereWithoutChallengeInput | StreakUpdateManyWithWhereWithoutChallengeInput[]
    deleteMany?: StreakScalarWhereInput | StreakScalarWhereInput[]
  }

  export type WaterEntryUncheckedUpdateManyWithoutChallengeNestedInput = {
    create?: XOR<WaterEntryCreateWithoutChallengeInput, WaterEntryUncheckedCreateWithoutChallengeInput> | WaterEntryCreateWithoutChallengeInput[] | WaterEntryUncheckedCreateWithoutChallengeInput[]
    connectOrCreate?: WaterEntryCreateOrConnectWithoutChallengeInput | WaterEntryCreateOrConnectWithoutChallengeInput[]
    upsert?: WaterEntryUpsertWithWhereUniqueWithoutChallengeInput | WaterEntryUpsertWithWhereUniqueWithoutChallengeInput[]
    createMany?: WaterEntryCreateManyChallengeInputEnvelope
    set?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    disconnect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    delete?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    connect?: WaterEntryWhereUniqueInput | WaterEntryWhereUniqueInput[]
    update?: WaterEntryUpdateWithWhereUniqueWithoutChallengeInput | WaterEntryUpdateWithWhereUniqueWithoutChallengeInput[]
    updateMany?: WaterEntryUpdateManyWithWhereWithoutChallengeInput | WaterEntryUpdateManyWithWhereWithoutChallengeInput[]
    deleteMany?: WaterEntryScalarWhereInput | WaterEntryScalarWhereInput[]
  }

  export type UserCreateNestedOneWithoutWaterEntriesInput = {
    create?: XOR<UserCreateWithoutWaterEntriesInput, UserUncheckedCreateWithoutWaterEntriesInput>
    connectOrCreate?: UserCreateOrConnectWithoutWaterEntriesInput
    connect?: UserWhereUniqueInput
  }

  export type ChallengeCreateNestedOneWithoutWaterEntriesInput = {
    create?: XOR<ChallengeCreateWithoutWaterEntriesInput, ChallengeUncheckedCreateWithoutWaterEntriesInput>
    connectOrCreate?: ChallengeCreateOrConnectWithoutWaterEntriesInput
    connect?: ChallengeWhereUniqueInput
  }

  export type FloatFieldUpdateOperationsInput = {
    set?: number
    increment?: number
    decrement?: number
    multiply?: number
    divide?: number
  }

  export type UserUpdateOneRequiredWithoutWaterEntriesNestedInput = {
    create?: XOR<UserCreateWithoutWaterEntriesInput, UserUncheckedCreateWithoutWaterEntriesInput>
    connectOrCreate?: UserCreateOrConnectWithoutWaterEntriesInput
    upsert?: UserUpsertWithoutWaterEntriesInput
    connect?: UserWhereUniqueInput
    update?: XOR<XOR<UserUpdateToOneWithWhereWithoutWaterEntriesInput, UserUpdateWithoutWaterEntriesInput>, UserUncheckedUpdateWithoutWaterEntriesInput>
  }

  export type ChallengeUpdateOneRequiredWithoutWaterEntriesNestedInput = {
    create?: XOR<ChallengeCreateWithoutWaterEntriesInput, ChallengeUncheckedCreateWithoutWaterEntriesInput>
    connectOrCreate?: ChallengeCreateOrConnectWithoutWaterEntriesInput
    upsert?: ChallengeUpsertWithoutWaterEntriesInput
    connect?: ChallengeWhereUniqueInput
    update?: XOR<XOR<ChallengeUpdateToOneWithWhereWithoutWaterEntriesInput, ChallengeUpdateWithoutWaterEntriesInput>, ChallengeUncheckedUpdateWithoutWaterEntriesInput>
  }

  export type UserCreateNestedOneWithoutDailyEntriesInput = {
    create?: XOR<UserCreateWithoutDailyEntriesInput, UserUncheckedCreateWithoutDailyEntriesInput>
    connectOrCreate?: UserCreateOrConnectWithoutDailyEntriesInput
    connect?: UserWhereUniqueInput
  }

  export type ChallengeCreateNestedOneWithoutDailyTasksInput = {
    create?: XOR<ChallengeCreateWithoutDailyTasksInput, ChallengeUncheckedCreateWithoutDailyTasksInput>
    connectOrCreate?: ChallengeCreateOrConnectWithoutDailyTasksInput
    connect?: ChallengeWhereUniqueInput
  }

  export type NullableDateTimeFieldUpdateOperationsInput = {
    set?: Date | string | null
  }

  export type UserUpdateOneRequiredWithoutDailyEntriesNestedInput = {
    create?: XOR<UserCreateWithoutDailyEntriesInput, UserUncheckedCreateWithoutDailyEntriesInput>
    connectOrCreate?: UserCreateOrConnectWithoutDailyEntriesInput
    upsert?: UserUpsertWithoutDailyEntriesInput
    connect?: UserWhereUniqueInput
    update?: XOR<XOR<UserUpdateToOneWithWhereWithoutDailyEntriesInput, UserUpdateWithoutDailyEntriesInput>, UserUncheckedUpdateWithoutDailyEntriesInput>
  }

  export type ChallengeUpdateOneRequiredWithoutDailyTasksNestedInput = {
    create?: XOR<ChallengeCreateWithoutDailyTasksInput, ChallengeUncheckedCreateWithoutDailyTasksInput>
    connectOrCreate?: ChallengeCreateOrConnectWithoutDailyTasksInput
    upsert?: ChallengeUpsertWithoutDailyTasksInput
    connect?: ChallengeWhereUniqueInput
    update?: XOR<XOR<ChallengeUpdateToOneWithWhereWithoutDailyTasksInput, ChallengeUpdateWithoutDailyTasksInput>, ChallengeUncheckedUpdateWithoutDailyTasksInput>
  }

  export type UserCreateNestedOneWithoutStreaksInput = {
    create?: XOR<UserCreateWithoutStreaksInput, UserUncheckedCreateWithoutStreaksInput>
    connectOrCreate?: UserCreateOrConnectWithoutStreaksInput
    connect?: UserWhereUniqueInput
  }

  export type ChallengeCreateNestedOneWithoutStreaksInput = {
    create?: XOR<ChallengeCreateWithoutStreaksInput, ChallengeUncheckedCreateWithoutStreaksInput>
    connectOrCreate?: ChallengeCreateOrConnectWithoutStreaksInput
    connect?: ChallengeWhereUniqueInput
  }

  export type UserUpdateOneRequiredWithoutStreaksNestedInput = {
    create?: XOR<UserCreateWithoutStreaksInput, UserUncheckedCreateWithoutStreaksInput>
    connectOrCreate?: UserCreateOrConnectWithoutStreaksInput
    upsert?: UserUpsertWithoutStreaksInput
    connect?: UserWhereUniqueInput
    update?: XOR<XOR<UserUpdateToOneWithWhereWithoutStreaksInput, UserUpdateWithoutStreaksInput>, UserUncheckedUpdateWithoutStreaksInput>
  }

  export type ChallengeUpdateOneRequiredWithoutStreaksNestedInput = {
    create?: XOR<ChallengeCreateWithoutStreaksInput, ChallengeUncheckedCreateWithoutStreaksInput>
    connectOrCreate?: ChallengeCreateOrConnectWithoutStreaksInput
    upsert?: ChallengeUpsertWithoutStreaksInput
    connect?: ChallengeWhereUniqueInput
    update?: XOR<XOR<ChallengeUpdateToOneWithWhereWithoutStreaksInput, ChallengeUpdateWithoutStreaksInput>, ChallengeUncheckedUpdateWithoutStreaksInput>
  }

  export type NestedStringFilter<$PrismaModel = never> = {
    equals?: string | StringFieldRefInput<$PrismaModel>
    in?: string[] | ListStringFieldRefInput<$PrismaModel>
    notIn?: string[] | ListStringFieldRefInput<$PrismaModel>
    lt?: string | StringFieldRefInput<$PrismaModel>
    lte?: string | StringFieldRefInput<$PrismaModel>
    gt?: string | StringFieldRefInput<$PrismaModel>
    gte?: string | StringFieldRefInput<$PrismaModel>
    contains?: string | StringFieldRefInput<$PrismaModel>
    startsWith?: string | StringFieldRefInput<$PrismaModel>
    endsWith?: string | StringFieldRefInput<$PrismaModel>
    not?: NestedStringFilter<$PrismaModel> | string
  }

  export type NestedDateTimeFilter<$PrismaModel = never> = {
    equals?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    in?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel>
    notIn?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel>
    lt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    lte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    not?: NestedDateTimeFilter<$PrismaModel> | Date | string
  }

  export type NestedStringWithAggregatesFilter<$PrismaModel = never> = {
    equals?: string | StringFieldRefInput<$PrismaModel>
    in?: string[] | ListStringFieldRefInput<$PrismaModel>
    notIn?: string[] | ListStringFieldRefInput<$PrismaModel>
    lt?: string | StringFieldRefInput<$PrismaModel>
    lte?: string | StringFieldRefInput<$PrismaModel>
    gt?: string | StringFieldRefInput<$PrismaModel>
    gte?: string | StringFieldRefInput<$PrismaModel>
    contains?: string | StringFieldRefInput<$PrismaModel>
    startsWith?: string | StringFieldRefInput<$PrismaModel>
    endsWith?: string | StringFieldRefInput<$PrismaModel>
    not?: NestedStringWithAggregatesFilter<$PrismaModel> | string
    _count?: NestedIntFilter<$PrismaModel>
    _min?: NestedStringFilter<$PrismaModel>
    _max?: NestedStringFilter<$PrismaModel>
  }

  export type NestedIntFilter<$PrismaModel = never> = {
    equals?: number | IntFieldRefInput<$PrismaModel>
    in?: number[] | ListIntFieldRefInput<$PrismaModel>
    notIn?: number[] | ListIntFieldRefInput<$PrismaModel>
    lt?: number | IntFieldRefInput<$PrismaModel>
    lte?: number | IntFieldRefInput<$PrismaModel>
    gt?: number | IntFieldRefInput<$PrismaModel>
    gte?: number | IntFieldRefInput<$PrismaModel>
    not?: NestedIntFilter<$PrismaModel> | number
  }

  export type NestedDateTimeWithAggregatesFilter<$PrismaModel = never> = {
    equals?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    in?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel>
    notIn?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel>
    lt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    lte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    not?: NestedDateTimeWithAggregatesFilter<$PrismaModel> | Date | string
    _count?: NestedIntFilter<$PrismaModel>
    _min?: NestedDateTimeFilter<$PrismaModel>
    _max?: NestedDateTimeFilter<$PrismaModel>
  }

  export type NestedStringNullableFilter<$PrismaModel = never> = {
    equals?: string | StringFieldRefInput<$PrismaModel> | null
    in?: string[] | ListStringFieldRefInput<$PrismaModel> | null
    notIn?: string[] | ListStringFieldRefInput<$PrismaModel> | null
    lt?: string | StringFieldRefInput<$PrismaModel>
    lte?: string | StringFieldRefInput<$PrismaModel>
    gt?: string | StringFieldRefInput<$PrismaModel>
    gte?: string | StringFieldRefInput<$PrismaModel>
    contains?: string | StringFieldRefInput<$PrismaModel>
    startsWith?: string | StringFieldRefInput<$PrismaModel>
    endsWith?: string | StringFieldRefInput<$PrismaModel>
    not?: NestedStringNullableFilter<$PrismaModel> | string | null
  }

  export type NestedFloatNullableFilter<$PrismaModel = never> = {
    equals?: number | FloatFieldRefInput<$PrismaModel> | null
    in?: number[] | ListFloatFieldRefInput<$PrismaModel> | null
    notIn?: number[] | ListFloatFieldRefInput<$PrismaModel> | null
    lt?: number | FloatFieldRefInput<$PrismaModel>
    lte?: number | FloatFieldRefInput<$PrismaModel>
    gt?: number | FloatFieldRefInput<$PrismaModel>
    gte?: number | FloatFieldRefInput<$PrismaModel>
    not?: NestedFloatNullableFilter<$PrismaModel> | number | null
  }

  export type NestedBoolFilter<$PrismaModel = never> = {
    equals?: boolean | BooleanFieldRefInput<$PrismaModel>
    not?: NestedBoolFilter<$PrismaModel> | boolean
  }

  export type NestedStringNullableWithAggregatesFilter<$PrismaModel = never> = {
    equals?: string | StringFieldRefInput<$PrismaModel> | null
    in?: string[] | ListStringFieldRefInput<$PrismaModel> | null
    notIn?: string[] | ListStringFieldRefInput<$PrismaModel> | null
    lt?: string | StringFieldRefInput<$PrismaModel>
    lte?: string | StringFieldRefInput<$PrismaModel>
    gt?: string | StringFieldRefInput<$PrismaModel>
    gte?: string | StringFieldRefInput<$PrismaModel>
    contains?: string | StringFieldRefInput<$PrismaModel>
    startsWith?: string | StringFieldRefInput<$PrismaModel>
    endsWith?: string | StringFieldRefInput<$PrismaModel>
    not?: NestedStringNullableWithAggregatesFilter<$PrismaModel> | string | null
    _count?: NestedIntNullableFilter<$PrismaModel>
    _min?: NestedStringNullableFilter<$PrismaModel>
    _max?: NestedStringNullableFilter<$PrismaModel>
  }

  export type NestedIntNullableFilter<$PrismaModel = never> = {
    equals?: number | IntFieldRefInput<$PrismaModel> | null
    in?: number[] | ListIntFieldRefInput<$PrismaModel> | null
    notIn?: number[] | ListIntFieldRefInput<$PrismaModel> | null
    lt?: number | IntFieldRefInput<$PrismaModel>
    lte?: number | IntFieldRefInput<$PrismaModel>
    gt?: number | IntFieldRefInput<$PrismaModel>
    gte?: number | IntFieldRefInput<$PrismaModel>
    not?: NestedIntNullableFilter<$PrismaModel> | number | null
  }

  export type NestedIntWithAggregatesFilter<$PrismaModel = never> = {
    equals?: number | IntFieldRefInput<$PrismaModel>
    in?: number[] | ListIntFieldRefInput<$PrismaModel>
    notIn?: number[] | ListIntFieldRefInput<$PrismaModel>
    lt?: number | IntFieldRefInput<$PrismaModel>
    lte?: number | IntFieldRefInput<$PrismaModel>
    gt?: number | IntFieldRefInput<$PrismaModel>
    gte?: number | IntFieldRefInput<$PrismaModel>
    not?: NestedIntWithAggregatesFilter<$PrismaModel> | number
    _count?: NestedIntFilter<$PrismaModel>
    _avg?: NestedFloatFilter<$PrismaModel>
    _sum?: NestedIntFilter<$PrismaModel>
    _min?: NestedIntFilter<$PrismaModel>
    _max?: NestedIntFilter<$PrismaModel>
  }

  export type NestedFloatFilter<$PrismaModel = never> = {
    equals?: number | FloatFieldRefInput<$PrismaModel>
    in?: number[] | ListFloatFieldRefInput<$PrismaModel>
    notIn?: number[] | ListFloatFieldRefInput<$PrismaModel>
    lt?: number | FloatFieldRefInput<$PrismaModel>
    lte?: number | FloatFieldRefInput<$PrismaModel>
    gt?: number | FloatFieldRefInput<$PrismaModel>
    gte?: number | FloatFieldRefInput<$PrismaModel>
    not?: NestedFloatFilter<$PrismaModel> | number
  }

  export type NestedFloatNullableWithAggregatesFilter<$PrismaModel = never> = {
    equals?: number | FloatFieldRefInput<$PrismaModel> | null
    in?: number[] | ListFloatFieldRefInput<$PrismaModel> | null
    notIn?: number[] | ListFloatFieldRefInput<$PrismaModel> | null
    lt?: number | FloatFieldRefInput<$PrismaModel>
    lte?: number | FloatFieldRefInput<$PrismaModel>
    gt?: number | FloatFieldRefInput<$PrismaModel>
    gte?: number | FloatFieldRefInput<$PrismaModel>
    not?: NestedFloatNullableWithAggregatesFilter<$PrismaModel> | number | null
    _count?: NestedIntNullableFilter<$PrismaModel>
    _avg?: NestedFloatNullableFilter<$PrismaModel>
    _sum?: NestedFloatNullableFilter<$PrismaModel>
    _min?: NestedFloatNullableFilter<$PrismaModel>
    _max?: NestedFloatNullableFilter<$PrismaModel>
  }

  export type NestedBoolWithAggregatesFilter<$PrismaModel = never> = {
    equals?: boolean | BooleanFieldRefInput<$PrismaModel>
    not?: NestedBoolWithAggregatesFilter<$PrismaModel> | boolean
    _count?: NestedIntFilter<$PrismaModel>
    _min?: NestedBoolFilter<$PrismaModel>
    _max?: NestedBoolFilter<$PrismaModel>
  }

  export type NestedFloatWithAggregatesFilter<$PrismaModel = never> = {
    equals?: number | FloatFieldRefInput<$PrismaModel>
    in?: number[] | ListFloatFieldRefInput<$PrismaModel>
    notIn?: number[] | ListFloatFieldRefInput<$PrismaModel>
    lt?: number | FloatFieldRefInput<$PrismaModel>
    lte?: number | FloatFieldRefInput<$PrismaModel>
    gt?: number | FloatFieldRefInput<$PrismaModel>
    gte?: number | FloatFieldRefInput<$PrismaModel>
    not?: NestedFloatWithAggregatesFilter<$PrismaModel> | number
    _count?: NestedIntFilter<$PrismaModel>
    _avg?: NestedFloatFilter<$PrismaModel>
    _sum?: NestedFloatFilter<$PrismaModel>
    _min?: NestedFloatFilter<$PrismaModel>
    _max?: NestedFloatFilter<$PrismaModel>
  }

  export type NestedDateTimeNullableFilter<$PrismaModel = never> = {
    equals?: Date | string | DateTimeFieldRefInput<$PrismaModel> | null
    in?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel> | null
    notIn?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel> | null
    lt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    lte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    not?: NestedDateTimeNullableFilter<$PrismaModel> | Date | string | null
  }

  export type NestedDateTimeNullableWithAggregatesFilter<$PrismaModel = never> = {
    equals?: Date | string | DateTimeFieldRefInput<$PrismaModel> | null
    in?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel> | null
    notIn?: Date[] | string[] | ListDateTimeFieldRefInput<$PrismaModel> | null
    lt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    lte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gt?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    gte?: Date | string | DateTimeFieldRefInput<$PrismaModel>
    not?: NestedDateTimeNullableWithAggregatesFilter<$PrismaModel> | Date | string | null
    _count?: NestedIntNullableFilter<$PrismaModel>
    _min?: NestedDateTimeNullableFilter<$PrismaModel>
    _max?: NestedDateTimeNullableFilter<$PrismaModel>
  }

  export type ChallengeCreateWithoutUserInput = {
    id?: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    dailyTasks?: DailyEntryCreateNestedManyWithoutChallengeInput
    streaks?: StreakCreateNestedManyWithoutChallengeInput
    waterEntries?: WaterEntryCreateNestedManyWithoutChallengeInput
  }

  export type ChallengeUncheckedCreateWithoutUserInput = {
    id?: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    dailyTasks?: DailyEntryUncheckedCreateNestedManyWithoutChallengeInput
    streaks?: StreakUncheckedCreateNestedManyWithoutChallengeInput
    waterEntries?: WaterEntryUncheckedCreateNestedManyWithoutChallengeInput
  }

  export type ChallengeCreateOrConnectWithoutUserInput = {
    where: ChallengeWhereUniqueInput
    create: XOR<ChallengeCreateWithoutUserInput, ChallengeUncheckedCreateWithoutUserInput>
  }

  export type ChallengeCreateManyUserInputEnvelope = {
    data: ChallengeCreateManyUserInput | ChallengeCreateManyUserInput[]
    skipDuplicates?: boolean
  }

  export type StreakCreateWithoutUserInput = {
    id?: string
    length?: number
    missedDays?: number
    freezesUsed?: number
    lastUpdated?: Date | string
    isActive?: boolean
    lastCompletedDay?: number
    challenge: ChallengeCreateNestedOneWithoutStreaksInput
  }

  export type StreakUncheckedCreateWithoutUserInput = {
    id?: string
    challengeId: string
    length?: number
    missedDays?: number
    freezesUsed?: number
    lastUpdated?: Date | string
    isActive?: boolean
    lastCompletedDay?: number
  }

  export type StreakCreateOrConnectWithoutUserInput = {
    where: StreakWhereUniqueInput
    create: XOR<StreakCreateWithoutUserInput, StreakUncheckedCreateWithoutUserInput>
  }

  export type StreakCreateManyUserInputEnvelope = {
    data: StreakCreateManyUserInput | StreakCreateManyUserInput[]
    skipDuplicates?: boolean
  }

  export type DailyEntryCreateWithoutUserInput = {
    id?: string
    dayNumber: number
    date: Date | string
    completed?: boolean
    completedAt?: Date | string | null
    notes?: string | null
    value?: number | null
    challenge: ChallengeCreateNestedOneWithoutDailyTasksInput
  }

  export type DailyEntryUncheckedCreateWithoutUserInput = {
    id?: string
    challengeId: string
    dayNumber: number
    date: Date | string
    completed?: boolean
    completedAt?: Date | string | null
    notes?: string | null
    value?: number | null
  }

  export type DailyEntryCreateOrConnectWithoutUserInput = {
    where: DailyEntryWhereUniqueInput
    create: XOR<DailyEntryCreateWithoutUserInput, DailyEntryUncheckedCreateWithoutUserInput>
  }

  export type DailyEntryCreateManyUserInputEnvelope = {
    data: DailyEntryCreateManyUserInput | DailyEntryCreateManyUserInput[]
    skipDuplicates?: boolean
  }

  export type WaterEntryCreateWithoutUserInput = {
    id?: string
    date?: Date | string
    amount: number
    targetAmount: number
    completed?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    challenge: ChallengeCreateNestedOneWithoutWaterEntriesInput
  }

  export type WaterEntryUncheckedCreateWithoutUserInput = {
    id?: string
    challengeId: string
    date?: Date | string
    amount: number
    targetAmount: number
    completed?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
  }

  export type WaterEntryCreateOrConnectWithoutUserInput = {
    where: WaterEntryWhereUniqueInput
    create: XOR<WaterEntryCreateWithoutUserInput, WaterEntryUncheckedCreateWithoutUserInput>
  }

  export type WaterEntryCreateManyUserInputEnvelope = {
    data: WaterEntryCreateManyUserInput | WaterEntryCreateManyUserInput[]
    skipDuplicates?: boolean
  }

  export type ChallengeUpsertWithWhereUniqueWithoutUserInput = {
    where: ChallengeWhereUniqueInput
    update: XOR<ChallengeUpdateWithoutUserInput, ChallengeUncheckedUpdateWithoutUserInput>
    create: XOR<ChallengeCreateWithoutUserInput, ChallengeUncheckedCreateWithoutUserInput>
  }

  export type ChallengeUpdateWithWhereUniqueWithoutUserInput = {
    where: ChallengeWhereUniqueInput
    data: XOR<ChallengeUpdateWithoutUserInput, ChallengeUncheckedUpdateWithoutUserInput>
  }

  export type ChallengeUpdateManyWithWhereWithoutUserInput = {
    where: ChallengeScalarWhereInput
    data: XOR<ChallengeUpdateManyMutationInput, ChallengeUncheckedUpdateManyWithoutUserInput>
  }

  export type ChallengeScalarWhereInput = {
    AND?: ChallengeScalarWhereInput | ChallengeScalarWhereInput[]
    OR?: ChallengeScalarWhereInput[]
    NOT?: ChallengeScalarWhereInput | ChallengeScalarWhereInput[]
    id?: StringFilter<"Challenge"> | string
    userId?: StringFilter<"Challenge"> | string
    name?: StringFilter<"Challenge"> | string
    description?: StringNullableFilter<"Challenge"> | string | null
    type?: StringFilter<"Challenge"> | string
    category?: StringFilter<"Challenge"> | string
    duration?: IntFilter<"Challenge"> | number
    startDate?: DateTimeFilter<"Challenge"> | Date | string
    endDate?: DateTimeFilter<"Challenge"> | Date | string
    targetValue?: FloatNullableFilter<"Challenge"> | number | null
    unit?: StringNullableFilter<"Challenge"> | string | null
    color?: StringNullableFilter<"Challenge"> | string | null
    icon?: StringNullableFilter<"Challenge"> | string | null
    isActive?: BoolFilter<"Challenge"> | boolean
    createdAt?: DateTimeFilter<"Challenge"> | Date | string
    updatedAt?: DateTimeFilter<"Challenge"> | Date | string
  }

  export type StreakUpsertWithWhereUniqueWithoutUserInput = {
    where: StreakWhereUniqueInput
    update: XOR<StreakUpdateWithoutUserInput, StreakUncheckedUpdateWithoutUserInput>
    create: XOR<StreakCreateWithoutUserInput, StreakUncheckedCreateWithoutUserInput>
  }

  export type StreakUpdateWithWhereUniqueWithoutUserInput = {
    where: StreakWhereUniqueInput
    data: XOR<StreakUpdateWithoutUserInput, StreakUncheckedUpdateWithoutUserInput>
  }

  export type StreakUpdateManyWithWhereWithoutUserInput = {
    where: StreakScalarWhereInput
    data: XOR<StreakUpdateManyMutationInput, StreakUncheckedUpdateManyWithoutUserInput>
  }

  export type StreakScalarWhereInput = {
    AND?: StreakScalarWhereInput | StreakScalarWhereInput[]
    OR?: StreakScalarWhereInput[]
    NOT?: StreakScalarWhereInput | StreakScalarWhereInput[]
    id?: StringFilter<"Streak"> | string
    userId?: StringFilter<"Streak"> | string
    challengeId?: StringFilter<"Streak"> | string
    length?: IntFilter<"Streak"> | number
    missedDays?: IntFilter<"Streak"> | number
    freezesUsed?: IntFilter<"Streak"> | number
    lastUpdated?: DateTimeFilter<"Streak"> | Date | string
    isActive?: BoolFilter<"Streak"> | boolean
    lastCompletedDay?: IntFilter<"Streak"> | number
  }

  export type DailyEntryUpsertWithWhereUniqueWithoutUserInput = {
    where: DailyEntryWhereUniqueInput
    update: XOR<DailyEntryUpdateWithoutUserInput, DailyEntryUncheckedUpdateWithoutUserInput>
    create: XOR<DailyEntryCreateWithoutUserInput, DailyEntryUncheckedCreateWithoutUserInput>
  }

  export type DailyEntryUpdateWithWhereUniqueWithoutUserInput = {
    where: DailyEntryWhereUniqueInput
    data: XOR<DailyEntryUpdateWithoutUserInput, DailyEntryUncheckedUpdateWithoutUserInput>
  }

  export type DailyEntryUpdateManyWithWhereWithoutUserInput = {
    where: DailyEntryScalarWhereInput
    data: XOR<DailyEntryUpdateManyMutationInput, DailyEntryUncheckedUpdateManyWithoutUserInput>
  }

  export type DailyEntryScalarWhereInput = {
    AND?: DailyEntryScalarWhereInput | DailyEntryScalarWhereInput[]
    OR?: DailyEntryScalarWhereInput[]
    NOT?: DailyEntryScalarWhereInput | DailyEntryScalarWhereInput[]
    id?: StringFilter<"DailyEntry"> | string
    userId?: StringFilter<"DailyEntry"> | string
    challengeId?: StringFilter<"DailyEntry"> | string
    dayNumber?: IntFilter<"DailyEntry"> | number
    date?: DateTimeFilter<"DailyEntry"> | Date | string
    completed?: BoolFilter<"DailyEntry"> | boolean
    completedAt?: DateTimeNullableFilter<"DailyEntry"> | Date | string | null
    notes?: StringNullableFilter<"DailyEntry"> | string | null
    value?: FloatNullableFilter<"DailyEntry"> | number | null
  }

  export type WaterEntryUpsertWithWhereUniqueWithoutUserInput = {
    where: WaterEntryWhereUniqueInput
    update: XOR<WaterEntryUpdateWithoutUserInput, WaterEntryUncheckedUpdateWithoutUserInput>
    create: XOR<WaterEntryCreateWithoutUserInput, WaterEntryUncheckedCreateWithoutUserInput>
  }

  export type WaterEntryUpdateWithWhereUniqueWithoutUserInput = {
    where: WaterEntryWhereUniqueInput
    data: XOR<WaterEntryUpdateWithoutUserInput, WaterEntryUncheckedUpdateWithoutUserInput>
  }

  export type WaterEntryUpdateManyWithWhereWithoutUserInput = {
    where: WaterEntryScalarWhereInput
    data: XOR<WaterEntryUpdateManyMutationInput, WaterEntryUncheckedUpdateManyWithoutUserInput>
  }

  export type WaterEntryScalarWhereInput = {
    AND?: WaterEntryScalarWhereInput | WaterEntryScalarWhereInput[]
    OR?: WaterEntryScalarWhereInput[]
    NOT?: WaterEntryScalarWhereInput | WaterEntryScalarWhereInput[]
    id?: StringFilter<"WaterEntry"> | string
    userId?: StringFilter<"WaterEntry"> | string
    challengeId?: StringFilter<"WaterEntry"> | string
    date?: DateTimeFilter<"WaterEntry"> | Date | string
    amount?: FloatFilter<"WaterEntry"> | number
    targetAmount?: FloatFilter<"WaterEntry"> | number
    completed?: BoolFilter<"WaterEntry"> | boolean
    createdAt?: DateTimeFilter<"WaterEntry"> | Date | string
    updatedAt?: DateTimeFilter<"WaterEntry"> | Date | string
  }

  export type UserCreateWithoutChallengesInput = {
    id?: string
    email: string
    createdAt?: Date | string
    streaks?: StreakCreateNestedManyWithoutUserInput
    dailyEntries?: DailyEntryCreateNestedManyWithoutUserInput
    waterEntries?: WaterEntryCreateNestedManyWithoutUserInput
  }

  export type UserUncheckedCreateWithoutChallengesInput = {
    id?: string
    email: string
    createdAt?: Date | string
    streaks?: StreakUncheckedCreateNestedManyWithoutUserInput
    dailyEntries?: DailyEntryUncheckedCreateNestedManyWithoutUserInput
    waterEntries?: WaterEntryUncheckedCreateNestedManyWithoutUserInput
  }

  export type UserCreateOrConnectWithoutChallengesInput = {
    where: UserWhereUniqueInput
    create: XOR<UserCreateWithoutChallengesInput, UserUncheckedCreateWithoutChallengesInput>
  }

  export type DailyEntryCreateWithoutChallengeInput = {
    id?: string
    dayNumber: number
    date: Date | string
    completed?: boolean
    completedAt?: Date | string | null
    notes?: string | null
    value?: number | null
    user: UserCreateNestedOneWithoutDailyEntriesInput
  }

  export type DailyEntryUncheckedCreateWithoutChallengeInput = {
    id?: string
    userId: string
    dayNumber: number
    date: Date | string
    completed?: boolean
    completedAt?: Date | string | null
    notes?: string | null
    value?: number | null
  }

  export type DailyEntryCreateOrConnectWithoutChallengeInput = {
    where: DailyEntryWhereUniqueInput
    create: XOR<DailyEntryCreateWithoutChallengeInput, DailyEntryUncheckedCreateWithoutChallengeInput>
  }

  export type DailyEntryCreateManyChallengeInputEnvelope = {
    data: DailyEntryCreateManyChallengeInput | DailyEntryCreateManyChallengeInput[]
    skipDuplicates?: boolean
  }

  export type StreakCreateWithoutChallengeInput = {
    id?: string
    length?: number
    missedDays?: number
    freezesUsed?: number
    lastUpdated?: Date | string
    isActive?: boolean
    lastCompletedDay?: number
    user: UserCreateNestedOneWithoutStreaksInput
  }

  export type StreakUncheckedCreateWithoutChallengeInput = {
    id?: string
    userId: string
    length?: number
    missedDays?: number
    freezesUsed?: number
    lastUpdated?: Date | string
    isActive?: boolean
    lastCompletedDay?: number
  }

  export type StreakCreateOrConnectWithoutChallengeInput = {
    where: StreakWhereUniqueInput
    create: XOR<StreakCreateWithoutChallengeInput, StreakUncheckedCreateWithoutChallengeInput>
  }

  export type StreakCreateManyChallengeInputEnvelope = {
    data: StreakCreateManyChallengeInput | StreakCreateManyChallengeInput[]
    skipDuplicates?: boolean
  }

  export type WaterEntryCreateWithoutChallengeInput = {
    id?: string
    date?: Date | string
    amount: number
    targetAmount: number
    completed?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    user: UserCreateNestedOneWithoutWaterEntriesInput
  }

  export type WaterEntryUncheckedCreateWithoutChallengeInput = {
    id?: string
    userId: string
    date?: Date | string
    amount: number
    targetAmount: number
    completed?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
  }

  export type WaterEntryCreateOrConnectWithoutChallengeInput = {
    where: WaterEntryWhereUniqueInput
    create: XOR<WaterEntryCreateWithoutChallengeInput, WaterEntryUncheckedCreateWithoutChallengeInput>
  }

  export type WaterEntryCreateManyChallengeInputEnvelope = {
    data: WaterEntryCreateManyChallengeInput | WaterEntryCreateManyChallengeInput[]
    skipDuplicates?: boolean
  }

  export type UserUpsertWithoutChallengesInput = {
    update: XOR<UserUpdateWithoutChallengesInput, UserUncheckedUpdateWithoutChallengesInput>
    create: XOR<UserCreateWithoutChallengesInput, UserUncheckedCreateWithoutChallengesInput>
    where?: UserWhereInput
  }

  export type UserUpdateToOneWithWhereWithoutChallengesInput = {
    where?: UserWhereInput
    data: XOR<UserUpdateWithoutChallengesInput, UserUncheckedUpdateWithoutChallengesInput>
  }

  export type UserUpdateWithoutChallengesInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    streaks?: StreakUpdateManyWithoutUserNestedInput
    dailyEntries?: DailyEntryUpdateManyWithoutUserNestedInput
    waterEntries?: WaterEntryUpdateManyWithoutUserNestedInput
  }

  export type UserUncheckedUpdateWithoutChallengesInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    streaks?: StreakUncheckedUpdateManyWithoutUserNestedInput
    dailyEntries?: DailyEntryUncheckedUpdateManyWithoutUserNestedInput
    waterEntries?: WaterEntryUncheckedUpdateManyWithoutUserNestedInput
  }

  export type DailyEntryUpsertWithWhereUniqueWithoutChallengeInput = {
    where: DailyEntryWhereUniqueInput
    update: XOR<DailyEntryUpdateWithoutChallengeInput, DailyEntryUncheckedUpdateWithoutChallengeInput>
    create: XOR<DailyEntryCreateWithoutChallengeInput, DailyEntryUncheckedCreateWithoutChallengeInput>
  }

  export type DailyEntryUpdateWithWhereUniqueWithoutChallengeInput = {
    where: DailyEntryWhereUniqueInput
    data: XOR<DailyEntryUpdateWithoutChallengeInput, DailyEntryUncheckedUpdateWithoutChallengeInput>
  }

  export type DailyEntryUpdateManyWithWhereWithoutChallengeInput = {
    where: DailyEntryScalarWhereInput
    data: XOR<DailyEntryUpdateManyMutationInput, DailyEntryUncheckedUpdateManyWithoutChallengeInput>
  }

  export type StreakUpsertWithWhereUniqueWithoutChallengeInput = {
    where: StreakWhereUniqueInput
    update: XOR<StreakUpdateWithoutChallengeInput, StreakUncheckedUpdateWithoutChallengeInput>
    create: XOR<StreakCreateWithoutChallengeInput, StreakUncheckedCreateWithoutChallengeInput>
  }

  export type StreakUpdateWithWhereUniqueWithoutChallengeInput = {
    where: StreakWhereUniqueInput
    data: XOR<StreakUpdateWithoutChallengeInput, StreakUncheckedUpdateWithoutChallengeInput>
  }

  export type StreakUpdateManyWithWhereWithoutChallengeInput = {
    where: StreakScalarWhereInput
    data: XOR<StreakUpdateManyMutationInput, StreakUncheckedUpdateManyWithoutChallengeInput>
  }

  export type WaterEntryUpsertWithWhereUniqueWithoutChallengeInput = {
    where: WaterEntryWhereUniqueInput
    update: XOR<WaterEntryUpdateWithoutChallengeInput, WaterEntryUncheckedUpdateWithoutChallengeInput>
    create: XOR<WaterEntryCreateWithoutChallengeInput, WaterEntryUncheckedCreateWithoutChallengeInput>
  }

  export type WaterEntryUpdateWithWhereUniqueWithoutChallengeInput = {
    where: WaterEntryWhereUniqueInput
    data: XOR<WaterEntryUpdateWithoutChallengeInput, WaterEntryUncheckedUpdateWithoutChallengeInput>
  }

  export type WaterEntryUpdateManyWithWhereWithoutChallengeInput = {
    where: WaterEntryScalarWhereInput
    data: XOR<WaterEntryUpdateManyMutationInput, WaterEntryUncheckedUpdateManyWithoutChallengeInput>
  }

  export type UserCreateWithoutWaterEntriesInput = {
    id?: string
    email: string
    createdAt?: Date | string
    challenges?: ChallengeCreateNestedManyWithoutUserInput
    streaks?: StreakCreateNestedManyWithoutUserInput
    dailyEntries?: DailyEntryCreateNestedManyWithoutUserInput
  }

  export type UserUncheckedCreateWithoutWaterEntriesInput = {
    id?: string
    email: string
    createdAt?: Date | string
    challenges?: ChallengeUncheckedCreateNestedManyWithoutUserInput
    streaks?: StreakUncheckedCreateNestedManyWithoutUserInput
    dailyEntries?: DailyEntryUncheckedCreateNestedManyWithoutUserInput
  }

  export type UserCreateOrConnectWithoutWaterEntriesInput = {
    where: UserWhereUniqueInput
    create: XOR<UserCreateWithoutWaterEntriesInput, UserUncheckedCreateWithoutWaterEntriesInput>
  }

  export type ChallengeCreateWithoutWaterEntriesInput = {
    id?: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    user: UserCreateNestedOneWithoutChallengesInput
    dailyTasks?: DailyEntryCreateNestedManyWithoutChallengeInput
    streaks?: StreakCreateNestedManyWithoutChallengeInput
  }

  export type ChallengeUncheckedCreateWithoutWaterEntriesInput = {
    id?: string
    userId: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    dailyTasks?: DailyEntryUncheckedCreateNestedManyWithoutChallengeInput
    streaks?: StreakUncheckedCreateNestedManyWithoutChallengeInput
  }

  export type ChallengeCreateOrConnectWithoutWaterEntriesInput = {
    where: ChallengeWhereUniqueInput
    create: XOR<ChallengeCreateWithoutWaterEntriesInput, ChallengeUncheckedCreateWithoutWaterEntriesInput>
  }

  export type UserUpsertWithoutWaterEntriesInput = {
    update: XOR<UserUpdateWithoutWaterEntriesInput, UserUncheckedUpdateWithoutWaterEntriesInput>
    create: XOR<UserCreateWithoutWaterEntriesInput, UserUncheckedCreateWithoutWaterEntriesInput>
    where?: UserWhereInput
  }

  export type UserUpdateToOneWithWhereWithoutWaterEntriesInput = {
    where?: UserWhereInput
    data: XOR<UserUpdateWithoutWaterEntriesInput, UserUncheckedUpdateWithoutWaterEntriesInput>
  }

  export type UserUpdateWithoutWaterEntriesInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    challenges?: ChallengeUpdateManyWithoutUserNestedInput
    streaks?: StreakUpdateManyWithoutUserNestedInput
    dailyEntries?: DailyEntryUpdateManyWithoutUserNestedInput
  }

  export type UserUncheckedUpdateWithoutWaterEntriesInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    challenges?: ChallengeUncheckedUpdateManyWithoutUserNestedInput
    streaks?: StreakUncheckedUpdateManyWithoutUserNestedInput
    dailyEntries?: DailyEntryUncheckedUpdateManyWithoutUserNestedInput
  }

  export type ChallengeUpsertWithoutWaterEntriesInput = {
    update: XOR<ChallengeUpdateWithoutWaterEntriesInput, ChallengeUncheckedUpdateWithoutWaterEntriesInput>
    create: XOR<ChallengeCreateWithoutWaterEntriesInput, ChallengeUncheckedCreateWithoutWaterEntriesInput>
    where?: ChallengeWhereInput
  }

  export type ChallengeUpdateToOneWithWhereWithoutWaterEntriesInput = {
    where?: ChallengeWhereInput
    data: XOR<ChallengeUpdateWithoutWaterEntriesInput, ChallengeUncheckedUpdateWithoutWaterEntriesInput>
  }

  export type ChallengeUpdateWithoutWaterEntriesInput = {
    id?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    user?: UserUpdateOneRequiredWithoutChallengesNestedInput
    dailyTasks?: DailyEntryUpdateManyWithoutChallengeNestedInput
    streaks?: StreakUpdateManyWithoutChallengeNestedInput
  }

  export type ChallengeUncheckedUpdateWithoutWaterEntriesInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    dailyTasks?: DailyEntryUncheckedUpdateManyWithoutChallengeNestedInput
    streaks?: StreakUncheckedUpdateManyWithoutChallengeNestedInput
  }

  export type UserCreateWithoutDailyEntriesInput = {
    id?: string
    email: string
    createdAt?: Date | string
    challenges?: ChallengeCreateNestedManyWithoutUserInput
    streaks?: StreakCreateNestedManyWithoutUserInput
    waterEntries?: WaterEntryCreateNestedManyWithoutUserInput
  }

  export type UserUncheckedCreateWithoutDailyEntriesInput = {
    id?: string
    email: string
    createdAt?: Date | string
    challenges?: ChallengeUncheckedCreateNestedManyWithoutUserInput
    streaks?: StreakUncheckedCreateNestedManyWithoutUserInput
    waterEntries?: WaterEntryUncheckedCreateNestedManyWithoutUserInput
  }

  export type UserCreateOrConnectWithoutDailyEntriesInput = {
    where: UserWhereUniqueInput
    create: XOR<UserCreateWithoutDailyEntriesInput, UserUncheckedCreateWithoutDailyEntriesInput>
  }

  export type ChallengeCreateWithoutDailyTasksInput = {
    id?: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    user: UserCreateNestedOneWithoutChallengesInput
    streaks?: StreakCreateNestedManyWithoutChallengeInput
    waterEntries?: WaterEntryCreateNestedManyWithoutChallengeInput
  }

  export type ChallengeUncheckedCreateWithoutDailyTasksInput = {
    id?: string
    userId: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    streaks?: StreakUncheckedCreateNestedManyWithoutChallengeInput
    waterEntries?: WaterEntryUncheckedCreateNestedManyWithoutChallengeInput
  }

  export type ChallengeCreateOrConnectWithoutDailyTasksInput = {
    where: ChallengeWhereUniqueInput
    create: XOR<ChallengeCreateWithoutDailyTasksInput, ChallengeUncheckedCreateWithoutDailyTasksInput>
  }

  export type UserUpsertWithoutDailyEntriesInput = {
    update: XOR<UserUpdateWithoutDailyEntriesInput, UserUncheckedUpdateWithoutDailyEntriesInput>
    create: XOR<UserCreateWithoutDailyEntriesInput, UserUncheckedCreateWithoutDailyEntriesInput>
    where?: UserWhereInput
  }

  export type UserUpdateToOneWithWhereWithoutDailyEntriesInput = {
    where?: UserWhereInput
    data: XOR<UserUpdateWithoutDailyEntriesInput, UserUncheckedUpdateWithoutDailyEntriesInput>
  }

  export type UserUpdateWithoutDailyEntriesInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    challenges?: ChallengeUpdateManyWithoutUserNestedInput
    streaks?: StreakUpdateManyWithoutUserNestedInput
    waterEntries?: WaterEntryUpdateManyWithoutUserNestedInput
  }

  export type UserUncheckedUpdateWithoutDailyEntriesInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    challenges?: ChallengeUncheckedUpdateManyWithoutUserNestedInput
    streaks?: StreakUncheckedUpdateManyWithoutUserNestedInput
    waterEntries?: WaterEntryUncheckedUpdateManyWithoutUserNestedInput
  }

  export type ChallengeUpsertWithoutDailyTasksInput = {
    update: XOR<ChallengeUpdateWithoutDailyTasksInput, ChallengeUncheckedUpdateWithoutDailyTasksInput>
    create: XOR<ChallengeCreateWithoutDailyTasksInput, ChallengeUncheckedCreateWithoutDailyTasksInput>
    where?: ChallengeWhereInput
  }

  export type ChallengeUpdateToOneWithWhereWithoutDailyTasksInput = {
    where?: ChallengeWhereInput
    data: XOR<ChallengeUpdateWithoutDailyTasksInput, ChallengeUncheckedUpdateWithoutDailyTasksInput>
  }

  export type ChallengeUpdateWithoutDailyTasksInput = {
    id?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    user?: UserUpdateOneRequiredWithoutChallengesNestedInput
    streaks?: StreakUpdateManyWithoutChallengeNestedInput
    waterEntries?: WaterEntryUpdateManyWithoutChallengeNestedInput
  }

  export type ChallengeUncheckedUpdateWithoutDailyTasksInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    streaks?: StreakUncheckedUpdateManyWithoutChallengeNestedInput
    waterEntries?: WaterEntryUncheckedUpdateManyWithoutChallengeNestedInput
  }

  export type UserCreateWithoutStreaksInput = {
    id?: string
    email: string
    createdAt?: Date | string
    challenges?: ChallengeCreateNestedManyWithoutUserInput
    dailyEntries?: DailyEntryCreateNestedManyWithoutUserInput
    waterEntries?: WaterEntryCreateNestedManyWithoutUserInput
  }

  export type UserUncheckedCreateWithoutStreaksInput = {
    id?: string
    email: string
    createdAt?: Date | string
    challenges?: ChallengeUncheckedCreateNestedManyWithoutUserInput
    dailyEntries?: DailyEntryUncheckedCreateNestedManyWithoutUserInput
    waterEntries?: WaterEntryUncheckedCreateNestedManyWithoutUserInput
  }

  export type UserCreateOrConnectWithoutStreaksInput = {
    where: UserWhereUniqueInput
    create: XOR<UserCreateWithoutStreaksInput, UserUncheckedCreateWithoutStreaksInput>
  }

  export type ChallengeCreateWithoutStreaksInput = {
    id?: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    user: UserCreateNestedOneWithoutChallengesInput
    dailyTasks?: DailyEntryCreateNestedManyWithoutChallengeInput
    waterEntries?: WaterEntryCreateNestedManyWithoutChallengeInput
  }

  export type ChallengeUncheckedCreateWithoutStreaksInput = {
    id?: string
    userId: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
    dailyTasks?: DailyEntryUncheckedCreateNestedManyWithoutChallengeInput
    waterEntries?: WaterEntryUncheckedCreateNestedManyWithoutChallengeInput
  }

  export type ChallengeCreateOrConnectWithoutStreaksInput = {
    where: ChallengeWhereUniqueInput
    create: XOR<ChallengeCreateWithoutStreaksInput, ChallengeUncheckedCreateWithoutStreaksInput>
  }

  export type UserUpsertWithoutStreaksInput = {
    update: XOR<UserUpdateWithoutStreaksInput, UserUncheckedUpdateWithoutStreaksInput>
    create: XOR<UserCreateWithoutStreaksInput, UserUncheckedCreateWithoutStreaksInput>
    where?: UserWhereInput
  }

  export type UserUpdateToOneWithWhereWithoutStreaksInput = {
    where?: UserWhereInput
    data: XOR<UserUpdateWithoutStreaksInput, UserUncheckedUpdateWithoutStreaksInput>
  }

  export type UserUpdateWithoutStreaksInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    challenges?: ChallengeUpdateManyWithoutUserNestedInput
    dailyEntries?: DailyEntryUpdateManyWithoutUserNestedInput
    waterEntries?: WaterEntryUpdateManyWithoutUserNestedInput
  }

  export type UserUncheckedUpdateWithoutStreaksInput = {
    id?: StringFieldUpdateOperationsInput | string
    email?: StringFieldUpdateOperationsInput | string
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    challenges?: ChallengeUncheckedUpdateManyWithoutUserNestedInput
    dailyEntries?: DailyEntryUncheckedUpdateManyWithoutUserNestedInput
    waterEntries?: WaterEntryUncheckedUpdateManyWithoutUserNestedInput
  }

  export type ChallengeUpsertWithoutStreaksInput = {
    update: XOR<ChallengeUpdateWithoutStreaksInput, ChallengeUncheckedUpdateWithoutStreaksInput>
    create: XOR<ChallengeCreateWithoutStreaksInput, ChallengeUncheckedCreateWithoutStreaksInput>
    where?: ChallengeWhereInput
  }

  export type ChallengeUpdateToOneWithWhereWithoutStreaksInput = {
    where?: ChallengeWhereInput
    data: XOR<ChallengeUpdateWithoutStreaksInput, ChallengeUncheckedUpdateWithoutStreaksInput>
  }

  export type ChallengeUpdateWithoutStreaksInput = {
    id?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    user?: UserUpdateOneRequiredWithoutChallengesNestedInput
    dailyTasks?: DailyEntryUpdateManyWithoutChallengeNestedInput
    waterEntries?: WaterEntryUpdateManyWithoutChallengeNestedInput
  }

  export type ChallengeUncheckedUpdateWithoutStreaksInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    dailyTasks?: DailyEntryUncheckedUpdateManyWithoutChallengeNestedInput
    waterEntries?: WaterEntryUncheckedUpdateManyWithoutChallengeNestedInput
  }

  export type ChallengeCreateManyUserInput = {
    id?: string
    name: string
    description?: string | null
    type: string
    category: string
    duration: number
    startDate: Date | string
    endDate: Date | string
    targetValue?: number | null
    unit?: string | null
    color?: string | null
    icon?: string | null
    isActive?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
  }

  export type StreakCreateManyUserInput = {
    id?: string
    challengeId: string
    length?: number
    missedDays?: number
    freezesUsed?: number
    lastUpdated?: Date | string
    isActive?: boolean
    lastCompletedDay?: number
  }

  export type DailyEntryCreateManyUserInput = {
    id?: string
    challengeId: string
    dayNumber: number
    date: Date | string
    completed?: boolean
    completedAt?: Date | string | null
    notes?: string | null
    value?: number | null
  }

  export type WaterEntryCreateManyUserInput = {
    id?: string
    challengeId: string
    date?: Date | string
    amount: number
    targetAmount: number
    completed?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
  }

  export type ChallengeUpdateWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    dailyTasks?: DailyEntryUpdateManyWithoutChallengeNestedInput
    streaks?: StreakUpdateManyWithoutChallengeNestedInput
    waterEntries?: WaterEntryUpdateManyWithoutChallengeNestedInput
  }

  export type ChallengeUncheckedUpdateWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    dailyTasks?: DailyEntryUncheckedUpdateManyWithoutChallengeNestedInput
    streaks?: StreakUncheckedUpdateManyWithoutChallengeNestedInput
    waterEntries?: WaterEntryUncheckedUpdateManyWithoutChallengeNestedInput
  }

  export type ChallengeUncheckedUpdateManyWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    name?: StringFieldUpdateOperationsInput | string
    description?: NullableStringFieldUpdateOperationsInput | string | null
    type?: StringFieldUpdateOperationsInput | string
    category?: StringFieldUpdateOperationsInput | string
    duration?: IntFieldUpdateOperationsInput | number
    startDate?: DateTimeFieldUpdateOperationsInput | Date | string
    endDate?: DateTimeFieldUpdateOperationsInput | Date | string
    targetValue?: NullableFloatFieldUpdateOperationsInput | number | null
    unit?: NullableStringFieldUpdateOperationsInput | string | null
    color?: NullableStringFieldUpdateOperationsInput | string | null
    icon?: NullableStringFieldUpdateOperationsInput | string | null
    isActive?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }

  export type StreakUpdateWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    length?: IntFieldUpdateOperationsInput | number
    missedDays?: IntFieldUpdateOperationsInput | number
    freezesUsed?: IntFieldUpdateOperationsInput | number
    lastUpdated?: DateTimeFieldUpdateOperationsInput | Date | string
    isActive?: BoolFieldUpdateOperationsInput | boolean
    lastCompletedDay?: IntFieldUpdateOperationsInput | number
    challenge?: ChallengeUpdateOneRequiredWithoutStreaksNestedInput
  }

  export type StreakUncheckedUpdateWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    length?: IntFieldUpdateOperationsInput | number
    missedDays?: IntFieldUpdateOperationsInput | number
    freezesUsed?: IntFieldUpdateOperationsInput | number
    lastUpdated?: DateTimeFieldUpdateOperationsInput | Date | string
    isActive?: BoolFieldUpdateOperationsInput | boolean
    lastCompletedDay?: IntFieldUpdateOperationsInput | number
  }

  export type StreakUncheckedUpdateManyWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    length?: IntFieldUpdateOperationsInput | number
    missedDays?: IntFieldUpdateOperationsInput | number
    freezesUsed?: IntFieldUpdateOperationsInput | number
    lastUpdated?: DateTimeFieldUpdateOperationsInput | Date | string
    isActive?: BoolFieldUpdateOperationsInput | boolean
    lastCompletedDay?: IntFieldUpdateOperationsInput | number
  }

  export type DailyEntryUpdateWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    dayNumber?: IntFieldUpdateOperationsInput | number
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    completed?: BoolFieldUpdateOperationsInput | boolean
    completedAt?: NullableDateTimeFieldUpdateOperationsInput | Date | string | null
    notes?: NullableStringFieldUpdateOperationsInput | string | null
    value?: NullableFloatFieldUpdateOperationsInput | number | null
    challenge?: ChallengeUpdateOneRequiredWithoutDailyTasksNestedInput
  }

  export type DailyEntryUncheckedUpdateWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    dayNumber?: IntFieldUpdateOperationsInput | number
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    completed?: BoolFieldUpdateOperationsInput | boolean
    completedAt?: NullableDateTimeFieldUpdateOperationsInput | Date | string | null
    notes?: NullableStringFieldUpdateOperationsInput | string | null
    value?: NullableFloatFieldUpdateOperationsInput | number | null
  }

  export type DailyEntryUncheckedUpdateManyWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    dayNumber?: IntFieldUpdateOperationsInput | number
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    completed?: BoolFieldUpdateOperationsInput | boolean
    completedAt?: NullableDateTimeFieldUpdateOperationsInput | Date | string | null
    notes?: NullableStringFieldUpdateOperationsInput | string | null
    value?: NullableFloatFieldUpdateOperationsInput | number | null
  }

  export type WaterEntryUpdateWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    amount?: FloatFieldUpdateOperationsInput | number
    targetAmount?: FloatFieldUpdateOperationsInput | number
    completed?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    challenge?: ChallengeUpdateOneRequiredWithoutWaterEntriesNestedInput
  }

  export type WaterEntryUncheckedUpdateWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    amount?: FloatFieldUpdateOperationsInput | number
    targetAmount?: FloatFieldUpdateOperationsInput | number
    completed?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }

  export type WaterEntryUncheckedUpdateManyWithoutUserInput = {
    id?: StringFieldUpdateOperationsInput | string
    challengeId?: StringFieldUpdateOperationsInput | string
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    amount?: FloatFieldUpdateOperationsInput | number
    targetAmount?: FloatFieldUpdateOperationsInput | number
    completed?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }

  export type DailyEntryCreateManyChallengeInput = {
    id?: string
    userId: string
    dayNumber: number
    date: Date | string
    completed?: boolean
    completedAt?: Date | string | null
    notes?: string | null
    value?: number | null
  }

  export type StreakCreateManyChallengeInput = {
    id?: string
    userId: string
    length?: number
    missedDays?: number
    freezesUsed?: number
    lastUpdated?: Date | string
    isActive?: boolean
    lastCompletedDay?: number
  }

  export type WaterEntryCreateManyChallengeInput = {
    id?: string
    userId: string
    date?: Date | string
    amount: number
    targetAmount: number
    completed?: boolean
    createdAt?: Date | string
    updatedAt?: Date | string
  }

  export type DailyEntryUpdateWithoutChallengeInput = {
    id?: StringFieldUpdateOperationsInput | string
    dayNumber?: IntFieldUpdateOperationsInput | number
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    completed?: BoolFieldUpdateOperationsInput | boolean
    completedAt?: NullableDateTimeFieldUpdateOperationsInput | Date | string | null
    notes?: NullableStringFieldUpdateOperationsInput | string | null
    value?: NullableFloatFieldUpdateOperationsInput | number | null
    user?: UserUpdateOneRequiredWithoutDailyEntriesNestedInput
  }

  export type DailyEntryUncheckedUpdateWithoutChallengeInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    dayNumber?: IntFieldUpdateOperationsInput | number
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    completed?: BoolFieldUpdateOperationsInput | boolean
    completedAt?: NullableDateTimeFieldUpdateOperationsInput | Date | string | null
    notes?: NullableStringFieldUpdateOperationsInput | string | null
    value?: NullableFloatFieldUpdateOperationsInput | number | null
  }

  export type DailyEntryUncheckedUpdateManyWithoutChallengeInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    dayNumber?: IntFieldUpdateOperationsInput | number
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    completed?: BoolFieldUpdateOperationsInput | boolean
    completedAt?: NullableDateTimeFieldUpdateOperationsInput | Date | string | null
    notes?: NullableStringFieldUpdateOperationsInput | string | null
    value?: NullableFloatFieldUpdateOperationsInput | number | null
  }

  export type StreakUpdateWithoutChallengeInput = {
    id?: StringFieldUpdateOperationsInput | string
    length?: IntFieldUpdateOperationsInput | number
    missedDays?: IntFieldUpdateOperationsInput | number
    freezesUsed?: IntFieldUpdateOperationsInput | number
    lastUpdated?: DateTimeFieldUpdateOperationsInput | Date | string
    isActive?: BoolFieldUpdateOperationsInput | boolean
    lastCompletedDay?: IntFieldUpdateOperationsInput | number
    user?: UserUpdateOneRequiredWithoutStreaksNestedInput
  }

  export type StreakUncheckedUpdateWithoutChallengeInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    length?: IntFieldUpdateOperationsInput | number
    missedDays?: IntFieldUpdateOperationsInput | number
    freezesUsed?: IntFieldUpdateOperationsInput | number
    lastUpdated?: DateTimeFieldUpdateOperationsInput | Date | string
    isActive?: BoolFieldUpdateOperationsInput | boolean
    lastCompletedDay?: IntFieldUpdateOperationsInput | number
  }

  export type StreakUncheckedUpdateManyWithoutChallengeInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    length?: IntFieldUpdateOperationsInput | number
    missedDays?: IntFieldUpdateOperationsInput | number
    freezesUsed?: IntFieldUpdateOperationsInput | number
    lastUpdated?: DateTimeFieldUpdateOperationsInput | Date | string
    isActive?: BoolFieldUpdateOperationsInput | boolean
    lastCompletedDay?: IntFieldUpdateOperationsInput | number
  }

  export type WaterEntryUpdateWithoutChallengeInput = {
    id?: StringFieldUpdateOperationsInput | string
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    amount?: FloatFieldUpdateOperationsInput | number
    targetAmount?: FloatFieldUpdateOperationsInput | number
    completed?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
    user?: UserUpdateOneRequiredWithoutWaterEntriesNestedInput
  }

  export type WaterEntryUncheckedUpdateWithoutChallengeInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    amount?: FloatFieldUpdateOperationsInput | number
    targetAmount?: FloatFieldUpdateOperationsInput | number
    completed?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }

  export type WaterEntryUncheckedUpdateManyWithoutChallengeInput = {
    id?: StringFieldUpdateOperationsInput | string
    userId?: StringFieldUpdateOperationsInput | string
    date?: DateTimeFieldUpdateOperationsInput | Date | string
    amount?: FloatFieldUpdateOperationsInput | number
    targetAmount?: FloatFieldUpdateOperationsInput | number
    completed?: BoolFieldUpdateOperationsInput | boolean
    createdAt?: DateTimeFieldUpdateOperationsInput | Date | string
    updatedAt?: DateTimeFieldUpdateOperationsInput | Date | string
  }



  /**
   * Aliases for legacy arg types
   */
    /**
     * @deprecated Use UserCountOutputTypeDefaultArgs instead
     */
    export type UserCountOutputTypeArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = UserCountOutputTypeDefaultArgs<ExtArgs>
    /**
     * @deprecated Use ChallengeCountOutputTypeDefaultArgs instead
     */
    export type ChallengeCountOutputTypeArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = ChallengeCountOutputTypeDefaultArgs<ExtArgs>
    /**
     * @deprecated Use UserDefaultArgs instead
     */
    export type UserArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = UserDefaultArgs<ExtArgs>
    /**
     * @deprecated Use ChallengeDefaultArgs instead
     */
    export type ChallengeArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = ChallengeDefaultArgs<ExtArgs>
    /**
     * @deprecated Use WaterEntryDefaultArgs instead
     */
    export type WaterEntryArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = WaterEntryDefaultArgs<ExtArgs>
    /**
     * @deprecated Use DailyEntryDefaultArgs instead
     */
    export type DailyEntryArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = DailyEntryDefaultArgs<ExtArgs>
    /**
     * @deprecated Use StreakDefaultArgs instead
     */
    export type StreakArgs<ExtArgs extends $Extensions.InternalArgs = $Extensions.DefaultArgs> = StreakDefaultArgs<ExtArgs>

  /**
   * Batch Payload for updateMany & deleteMany & createMany
   */

  export type BatchPayload = {
    count: number
  }

  /**
   * DMMF
   */
  export const dmmf: runtime.BaseDMMF
}