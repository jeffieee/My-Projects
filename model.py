import torch
import torch.nn as nn
import torch.optim as optim
import torchvision.transforms as transforms
from torch.utils.data import DataLoader, Dataset
from torchvision.datasets import ImageFolder
from torchvision import models
from torch.optim.lr_scheduler import StepLR
from PIL import Image
import torch.nn.functional as F

# Inception module definition
class InceptionBlock(nn.Module):
    def __init__(self, in_channels, out_channels):
        super(InceptionBlock, self).__init__()
        self.conv1x1_1 = nn.Conv2d(in_channels, out_channels // 4, kernel_size=1)
        self.conv3x3 = nn.Conv2d(in_channels, out_channels // 4, kernel_size=3, padding=1)
        self.conv5x5 = nn.Conv2d(in_channels, out_channels // 4, kernel_size=5, padding=2)
        self.pool = nn.Conv2d(in_channels, out_channels // 4, kernel_size=1)
        
    def forward(self, x):
        conv1x1_1 = self.conv1x1_1(x)
        conv3x3 = self.conv3x3(x)
        conv5x5 = self.conv5x5(x)
        pool = F.avg_pool2d(x, kernel_size=3, stride=1, padding=1)
        pool = self.pool(pool)
        
        output = torch.cat([conv1x1_1, conv3x3, conv5x5, pool], dim=1)
        return output

# LeNet architecture definition with Inception module
class LeNetWithInception(nn.Module):
    def __init__(self, num_classes):
        super(LeNetWithInception, self).__init__()
        self.relu = nn.ReLU()
        self.pool = nn.AvgPool2d(kernel_size=2, stride=2)
        self.conv1 = nn.Conv2d(
            in_channels=1,
            out_channels=6,
            kernel_size=5,
            stride=1,
            padding=0,
        )
       
        self.conv2 = nn.Conv2d(
            in_channels=6,
            out_channels=16,
            kernel_size=5,
            stride=1,
            padding=0,
        )
        self.inception1 = InceptionBlock(in_channels=16, out_channels=16)
        
        self.conv3 = nn.Conv2d(
            in_channels=16,
            out_channels=120,
            kernel_size=5,
            stride=1,
            padding=0,
        )
        self.linear1 = nn.Linear(120, 84)
        self.linear2 = nn.Linear(84, num_classes)

    def forward(self, x):
        x = self.relu(self.conv1(x))
        x = self.pool(x)
        x = self.relu(self.conv2(x))
        x = self.pool(x)
        x = self.inception1(x)
        x = self.relu(self.conv3(x))
        x = x.view(x.size(0), -1)
        x = self.relu(self.linear1(x))
        x = self.linear2(x)
        return x
    
class CustomDataset(Dataset):
    def __init__(self, data_dir, transform=None):
        self.dataset = ImageFolder(data_dir, transform=transform)

    def __len__(self):
        return len(self.dataset)

    def __getitem__(self, idx):
        return self.dataset[idx]

def train(model, train_loader, criterion, optimizer, scheduler, device):
    model.train()
    train_loss = 0.0

    for images, labels in train_loader:
        images, labels = images.to(device), labels.to(device)

        optimizer.zero_grad()
        outputs = model(images)
        loss = criterion(outputs, labels)
        loss.backward()
        optimizer.step()

        train_loss += loss.item() * images.size(0)

    scheduler.step()  # Update learning rate scheduler
    return train_loss / len(train_loader.dataset)

def validate(model, val_loader, criterion, device):
    model.eval()
    val_loss = 0.0

    with torch.no_grad():
        for images, labels in val_loader:
            images, labels = images.to(device), labels.to(device)
            outputs = model(images)
            loss = criterion(outputs, labels)
            val_loss += loss.item() * images.size(0)

    return val_loss / len(val_loader.dataset)

def predict(model, image, transform, class_names):
    model.eval()
    with torch.no_grad():
        image = transform(image).unsqueeze(0)
        output = model(image)
        _, predicted = torch.max(output, 1)
        predicted_class = class_names[predicted.item()]
    return predicted_class

def calculate_accuracy(model, dataloader, device):
    model.eval()
    correct_predictions = 0
    total_samples = 0

    with torch.no_grad():
        for images, labels in dataloader:
            images, labels = images.to(device), labels.to(device)
            outputs = model(images)
            _, predicted = torch.max(outputs, 1)
            correct_predictions += (predicted == labels).sum().item()
            total_samples += labels.size(0)

    accuracy = correct_predictions / total_samples
    return accuracy

def validate(model, dataloader, criterion, device):
    model.eval()
    val_loss = 0.0
    correct_predictions = 0
    total_samples = 0

    with torch.no_grad():
        for inputs, labels in dataloader:
            inputs, labels = inputs.to(device), labels.to(device)

            outputs = model(inputs)
            loss = criterion(outputs, labels)
            val_loss += loss.item()

            _, predicted = torch.max(outputs, 1)
            correct_predictions += (predicted == labels).sum().item()
            total_samples += labels.size(0)

    accuracy = correct_predictions / total_samples
    val_loss /= len(dataloader)

    return val_loss, accuracy

if __name__ == "__main__":
    data_dir = "C:\\Users\\Eric Garcia\\OneDrive\\Desktop\\Inception-Lenet\\Model\\studysession\\datasets"
    num_classes = 17
    batch_size = 25
    num_epochs = 30  
    learning_rate = 0.001

    transform = transforms.Compose([
        transforms.Resize((32, 32)),
        transforms.Grayscale(num_output_channels=1), 
        transforms.ToTensor()
        
    ])

    train_dataset = CustomDataset(data_dir + "/train", transform=transform)
    test_dataset = CustomDataset(data_dir + "/test", transform=transform)

    train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)
    test_loader = DataLoader(test_dataset, batch_size=batch_size, shuffle=False)
    
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model = LeNetWithInception(num_classes).to(device)
    criterion = nn.CrossEntropyLoss()
    optimizer = optim.Adam(model.parameters(), lr=learning_rate)
    
    # Use a learning rate scheduler to adjust the learning rate during training
    scheduler = StepLR(optimizer, step_size=5, gamma=0.8)

    class_names = ['a', 'ba', 'dara', 'ei', 'ga', 'ha', 'ka', 'la', 'ma', 'na', 'nga', 'ou', 'pa', 'sa', 'ta', 'wa', 'ya']  

    for epoch in range(num_epochs):
        train_loss = train(model, train_loader, criterion, optimizer, scheduler, device)
        val_loss, val_accuracy = validate(model, test_loader, criterion, device)
        print(f"Epoch [{epoch+1}/{num_epochs}] - Train Loss: {train_loss:.4f}, Val Loss: {val_loss:.4f}, Val Accuracy: {val_accuracy:.4f}")
        torch.save({
            'epoch': epoch,
            'model_state_dict': model.state_dict(),
            'optimizer_state_dict': optimizer.state_dict(),
            # Other relevant information you want to save
        }, 'myModel.pth')
        # torch.save(optimizer.state_dict(), 'myModel.pth')
    
    # Calculate accuracy on the test set
    test_accuracy = calculate_accuracy(model, test_loader, device)
    print(f"Test Accuracy: {test_accuracy:.4f}")

    # Example prediction
    example_image = Image.open("C:\\Users\\Eric Garcia\\OneDrive\\Desktop\\Inception-Lenet\\Model\\studysession\\datasets\\test\\a\\Your Image Title (1).jpg")
    predicted_class = predict(model, example_image, transform, class_names)
    print("Predicted class:", predicted_class)