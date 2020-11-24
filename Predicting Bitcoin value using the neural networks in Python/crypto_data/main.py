import pandas as pd
import os
from sklearn import preprocessing
from collections import deque
import random
import numpy as np
import time
import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.python.keras.layers import Dense, Dropout, LSTM, CuDNNLSTM, BatchNormalization
from tensorflow.keras.callbacks import TensorBoard, ModelCheckpoint
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
#CONSTANTS

SEQ_LEN = 60 #USING LAST 60 MIN
FUTURE_PERIOD_PREDICT = 3 #PREDICTING NEXT 3 MIN
RATIO_TO_PREDICT = "BTC-USD"
EPOCHS = 10 #10
BATCH_SIZE = 30 #64
NAME = f"{RATIO_TO_PREDICT}-{SEQ_LEN}-SEQ-{FUTURE_PERIOD_PREDICT}-PRED-{int(time.time())}"


def classify(current, future):
    if float(future) > float(current):
        return 1 #if price is higher -> we should buy it
    else:
        return 0

def preprocess_df(df):
    df = df.drop('future',1)

    for col in df.columns:
        if col != "target":
            #Currency fluctations
            df[col] = df[col].pct_change()
            #getting rid of NA values
            df.dropna(inplace=True)
            #scaling the value by keras
            df[col] = preprocessing.scale(df[col].values)

    df.dropna(inplace=True)

    sequential_data = []
    prev_days = deque(maxlen=SEQ_LEN)

    for i in df.values:
        prev_days.append([n for n in i[:-1]])
        if len(prev_days) == SEQ_LEN:
            sequential_data.append([np.array(prev_days), i[-1]])
    random.shuffle(sequential_data)

    #balancing the data
    buys = []
    sells = []

    for seq, target in sequential_data:
        if target == 0:
            sells.append([seq,target])
        elif target == 1:
            buys.append([seq,target])

    random.shuffle(buys)
    random.shuffle(sells)

    lower = min(len(buys),len(sells))
    #who is min?
    #30K
    buys = buys[:lower] #up tp lower
    sells = sells[:lower]

    sequential_data = buys + sells
    random.shuffle(sequential_data)

    X = []
    y = []

    for seq, target in sequential_data:
        X.append(seq)
        y.append(target)

    return np.array(X), y


main_df = pd.DataFrame()
ratios = ["BTC-USD","LTC-USD","ETH-USD","BCH-USD"]
for ratio in ratios:
     dataset = f"D:\crypto_data\crypto_data\{ratio}.csv"

     df = pd.read_csv(dataset,names=["time","low","high","open","close","volume"])
     df.rename(columns={"close": f"{ratio}_close", "volume": f"{ratio}_volume"}, inplace=True)
#WE PUT ONLY THE CLOSE VALUE AND VOLUME INTO THE DATA FRAME, REDUCING THE OTHER COLUMNS
     df.set_index("time",inplace=True)

     df = df[[f"{ratio}_close",f"{ratio}_volume"]]

     if len(main_df) == 0:
         main_df = df
     else:
         main_df = main_df.join(df)

main_df.fillna(method="ffill", inplace=True)  # if there are gaps in data, use previously known values NEW
main_df.dropna(inplace=True) #NEW

main_df['future'] = main_df[f"{RATIO_TO_PREDICT}_close"].shift(-FUTURE_PERIOD_PREDICT)
main_df['target'] = list(map(classify,main_df[f"{RATIO_TO_PREDICT}_close"],main_df["future"]))
print(main_df[[f"{RATIO_TO_PREDICT}_close", "future", "target"]].head())
main_df.dropna(inplace=True) #NEW

#we have to separate out of sample data - part of normalizing
times = sorted(main_df.index.values)
last_5pct = times[-int(0.05*len(times))]
print(last_5pct)

validation_main_df = main_df[(main_df.index >= last_5pct)]
main_df = main_df[(main_df.index < last_5pct)]

#lets make sequences
#preprocess_df(main_df)
train_x, train_y = preprocess_df(main_df)
validation_x, validation_y = preprocess_df(validation_main_df)

print(f"train data: {len(train_x)} validation: {len(validation_x)}")
print(f"Dont buys: {train_y.count(0)}, buys: {train_y.count(1)}")
print(f"VALIDATION Dont buys: {validation_y.count(0)}, buys: {validation_y.count(1)}")

model = Sequential()
model.add(CuDNNLSTM(128, input_shape=(train_x.shape[1:]), return_sequences=True)) #128 train shape 1:a
model.add(Dropout(0.2)) #0.2
model.add(BatchNormalization())

model.add(CuDNNLSTM(128, return_sequences=True)) #128
model.add(Dropout(0.1)) #0.1
model.add(BatchNormalization())

model.add(CuDNNLSTM(128)) #128
model.add(Dropout(0.2)) #0.2
model.add(BatchNormalization())

model.add(Dense(32, activation='relu')) #32
model.add(Dropout(0.2))

model.add(Dense(2, activation='softmax')) #2 softmax

opt = tf.keras.optimizers.Adam(lr=0.001,decay=1e-6)

model.compile(loss='sparse_categorical_crossentropy', #sparse_categorical_crossentropy
              optimizer=opt, #opt
              metrics=['acc'])

tensorboard = TensorBoard(log_dir="D:\crypto_data\logs\{}".format(NAME))

filepath = "RNN_Final-{epoch:02d}-{val_acc:.3f}"  # unique file name that will include the epoch and the validation acc for that epoch
checkpoint = ModelCheckpoint("D:\crypto_data\models\{}.model".format(filepath, monitor='val_acc', verbose=1, save_best_only=True, mode='max')) # saves only the best ones

train_x = np.asarray(train_x)
train_y = np.asarray(train_y)
validation_x = np.asarray(validation_x)
validation_y = np.asarray(validation_y)

history = model.fit(
    train_x, train_y,
    batch_size=BATCH_SIZE,
    epochs=EPOCHS,
    validation_data=(validation_x,validation_y),
    callbacks=[tensorboard,checkpoint])

# Score model
score = model.evaluate(validation_x, validation_y, verbose=0)
print('Test loss:', score[0])
print('Test accuracy:', score[1])
# Save model
model.save("D:\crypto_data\models\{}".format(NAME))
